import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import vector5 from '../assets/svgs/vector5.svg';
import downArrow from '../assets/svgs/down-arrow.svg';
import User01c from '../assets/pngs/User01c.png';
import User02c from '../assets/pngs/User02c.png';
import User03c from '../assets/pngs/User03c.png';
import User04c from '../assets/pngs/User04c.png';
import { retrieveAlbumListApi } from '../apis/AlbumApiService';

export default function ListAlbumComponent() {
    const queryParams = {
        sort: "byName",
        keyword: "앨범"
    };

    const [albumList, setAlbumList] = useState([]);

    const navigate = useNavigate();

    useEffect(() => refreshAlbumList(), []);

    function refreshAlbumList() {
        retrieveAlbumListApi(queryParams)
        .then(response => {
            console.log(response.data);
            setAlbumList(response.data);
        })
        .catch(error => console.log(error));
    }
    
    function handleClick(id) {
        navigate(`/albums/${id}`);
    }

    const imageArray = [User01c, User02c, User03c, User04c, User01c];

    return (
        <div>
            <header className="border-bottom border-light border-5 mb-5 p-2" style={{marginTop: "-70px"}}>
                <div className="container">
                    <div className="row">
                        <nav className="navbar navbar-expand-lg">
                            <div className="collapse navbar-collapse">
                                <ul className="navbar-nav">
                                    <li className="nav-item fs-5 mt-5">
                                    <label className="AlbumLabel">Galleries</label>
                                    </li>
                                </ul>
                            </div>
                            <ul className="navbar-nav">
                                <li className="nav-item fs-5 mt-5">
                                    <label className="AlbumSortLabel">생성 날짜 순</label>
                                </li>
                                <li className="nav-item fs-5 mt-5">
                                    <button className="AlbumSortButton">
                                        <img src={downArrow} alt="down arrow" />
                                    </button>
                                </li>
                                <li className="nav-item fs-5 mt-5">
                                    <img src={vector5} alt="vector 5" style={{"marginLeft": "20px"}}/>
                                </li>
                                <li className="nav-item fs-5 mt-5">
                                    <input className="AlbumSearchInput" placeholder="갤러리 검색" type="text" />
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </header>
            <div>
                <div style={{maxWidth: "1300px", overflowX: "auto", marginLeft: "200px"}}>
                    <table className="table">
                        <thead>
                            <tr>
                                <td>앨범 이름</td>
                                <td>사진 수</td>
                                <td>썸네일</td>
                            </tr>
                        </thead>
                        <tbody>
                            {albumList.map((album, index) => (
                            <tr key={album.albumId}>
                                <td>{album.albumName}</td>
                                <td>{album.count}</td>
                                <td onClick={() => handleClick(album.albumId)} style={{ cursor: 'pointer' }}>
                                <div className="container">
                                    <div className="row">
                                        <div className="col">
                                            <img src={imageArray[index++]} alt={`user ${index + 1}`} className="img-thumbnail" />
                                        </div>
                                    </div>
                                </div>
                                </td>
                            </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    )   
}