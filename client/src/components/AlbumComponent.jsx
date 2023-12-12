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
import { addPhotoApi, movePhotoApi, deletePhotoApi, downloadPhotoApi } from '../apis/PhotoApiService';

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
        setSelectedFiles(event.target.files);
    };

    const handleUploadButtonClick = () => {
        if (!selectedFiles) {
            console.log('no selected file');
            return;
        }

        addPhoto();
    };

    function addPhoto() {
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
        photoIds: [352,353],
    }

    function movePhoto() {
        movePhotoApi(id, movePhotoDto)
        .then(response => {
            console.log(response.data);
        })
        .catch(error => console.log(error));
    }

    const photoIds = [252,253];

    function deletePhoto() {
        deletePhotoApi(id, photoIds)
        .then(response => {
            console.log("success to delete photos");
        })
        .catch(error => console.log(error));
    }

    const queryParams = {
        photoIds: photoIds.join(','),
    }

    async function downloadPhoto() {
        try {
            const response = await downloadPhotoApi(id, queryParams);
            console.log("success to download photos", response);
            
            const blob = new Blob([response.data]);

            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = 'photos.zip';
            
            link.click();
        } catch (error) {
            console.error(error);
        }
    }

    const imageArray = [User01c, User02c, User03c, User04c, User01c];

    return (
        <div>
            <div style={{"display": "flex", "flexDirection": "column"}}>
                <label className="AlbumNameLabel">{name}</label>
                <div>
                    <div className="FileUploadContainer">
                        <label className="FileLabel" htmlFor="chooseFile">사진 불러오기</label>
                        <input type="file" id="chooseFile" className="FileInput" onChange={handleFileChange} multiple />
                        <button className="AddPhotoButton" onClick={handleUploadButtonClick}>사진 추가</button>
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
                    <img src={download} alt={"download file"} onClick={downloadPhoto} />
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
