import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react';
import User01c from '../assets/pngs/User01c.png';
import User02c from '../assets/pngs/User02c.png';
import User03c from '../assets/pngs/User03c.png';
import User04c from '../assets/pngs/User04c.png';
import line from '../assets/svgs/horizontal-line.svg';
import driveFileMove from '../assets/svgs/drive-file-move.svg';
import trashCan from '../assets/svgs/trash-can.svg';
import download from '../assets/svgs/download.svg';
import { retrieveAlbumApi } from '../apis/AlbumApiService';
import { addPhotoApi, movePhotoApi, deletePhotoApi } from '../apis/PhotoApiService';

export default function AlbumComponent() {
    const {id} = useParams();

    const [name, setName] = useState('');
    const [date, setDate] = useState('');
    const [count, setCount] = useState(0);

    function retrieveAlbum() {
        retrieveAlbumApi(id)
        .then(response => {
            setName(response.data.albumName);
            setDate(response.data.createAt);
            setCount(response.data.count);
        })
        .catch(error => console.log(error));
    }

    const dateObject = new Date(date);

    const year = dateObject.getFullYear();
    const month = dateObject.getMonth() + 1;
    const day = dateObject.getDate();

    const fullDate = year + "-" + month + "-" + day;

    useEffect(
        () => retrieveAlbum(),
        [id]
    );

    const [selectedFiles, setSelectedFiles] = useState(null);

    function handleFileChange(event) {
        const files = event.target.files;
        
        setSelectedFiles(files);

        if (files) {
            addPhoto();
        }
    };

    function addPhoto() {
        if (!selectedFiles) {
            console.error('no selected file');
            return;
        }

        const formData = new FormData();

        Array.from(selectedFiles).forEach((file) => {
        formData.append('photos', file);
        });

        addPhotoApi(id, formData)
        .then(response => {
            console.log(response.data);
        })
        .catch(error => console.log(error));
    };

    const movePhotoDto = {
        fromAlbumId: 1,
        toAlbumId: 5,
        photoIds: [1,2,3],
    }

    function movePhoto() {
        movePhotoApi(id, movePhotoDto)
        .then(response => {
            console.log(response.data);
        })
        .catch(error => console.log(error));
    }

    const photoIds = [202,203,204];

    function deletePhoto() {
        deletePhotoApi(id, photoIds)
        .then(response => {
            console.log("success to delete photos");
        })
        .catch(error => console.log(error));
    }

    const imageArray = [User01c, User02c, User03c, User04c, User01c];

    return (
        <div>
            <div style={{"display": "flex", "flexDirection": "column"}}>
                <label className="AlbumNameLabel">{name}</label>
                <div>
                    <div className="FileUploadContainer">
                        <input type="file" onChange={handleFileChange} className="FileInput" />
                        <label className="AddPhotoButton" onClick={handleFileChange}>사진 추가</label>
                        <label className="AlbumDetailLabel">{fullDate} | {count}장</label>
                    </div>   
                </div>
            </div>
            <div className="AlbumDetailHeader">
                <button className="MovePhotoButton">
                    <img src={driveFileMove} alt={"move file"} onClick={movePhoto} />
                </button>
                <button className="DeletePhotoButton">
                    <img src={trashCan} alt={"delete file"} onClick={deletePhoto} />
                </button>
                <button className="DownloadPhotoButton">
                    <img src={download} alt={"download file"} />
                </button>
            </div>
            <div>
                <img src={line} alt={"line"} className="vector" />
            </div>
            <table className="table">
                <tbody>
                    <tr key={id}>
                        <td>
                            <div className="container">
                                <div className="row">
                                    {imageArray.map((image, index) => (
                                        <div className="col" key={index}>
                                            <img src={image} alt={`user ${index + 1}`} className="img-thumbnail" />
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    )
}
