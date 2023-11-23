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

export default function AlbumComponent() {
    const {id} = useParams();

    const [name, setName] = useState('');

    function retrieveAlbum() {
        retrieveAlbumApi(id)
        .then(response => {
            setName(response.data.albumName);
        })
        .catch(error => console.log(error));
    }

    useEffect(
        () => retrieveAlbum(),
        [id]
    );

    const imageArray = [User01c, User02c, User03c, User04c, User01c];

    return (
        <div>
            <div className="AlbumDetailHeader">
                <button className="MovePhotoButton">
                    <img src={driveFileMove} alt={"move file"} />
                </button>
                <button className="DeletePhotoButton">
                    <img src={trashCan} alt={"delete file"} />
                </button>
                <button className="DownloadPhotoButton">
                    <img src={download} alt={"download file"} />
                </button>
            </div>
            <div>
                <img src={line} alt={"line"} className="vector" />
            </div>
            <table className="table">
                <thead>
                    <tr>
                        <th>Photos</th>
                    </tr>
                </thead>
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
