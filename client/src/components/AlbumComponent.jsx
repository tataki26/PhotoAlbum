import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react';
import User01c from '../assets/pngs/User01c.png';
import User02c from '../assets/pngs/User02c.png';
import User03c from '../assets/pngs/User03c.png';
import User04c from '../assets/pngs/User04c.png';
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
            <table className="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Photos</th>
                    </tr>
                </thead>
                <tbody>
                    <tr key={id}>
                        <td>{id}</td>
                        <td>
                            <div className="container">
                                <div className="row">
                                    {imageArray.map((image, index) => (
                                        <div className="col" key={index}>
                                            <img src={image} alt={`user photo ${index + 1}`} className="img-thumbnail" />
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
