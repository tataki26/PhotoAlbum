import { useParams } from "react-router-dom";
import { useState, useEffect } from 'react';
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

    return (
        <h1>{name}</h1>
    )
}
