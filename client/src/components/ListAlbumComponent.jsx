import { useNavigate } from 'react-router-dom';
import vector5 from '../assets/svgs/vector5.svg';
import downArrow from '../assets/svgs/down-arrow.svg';
import User01c from '../assets/pngs/User01c.png';
import User02c from '../assets/pngs/User02c.png';
import User03c from '../assets/pngs/User03c.png';
import User04c from '../assets/pngs/User04c.png';

export default function ListAlbumComponent() {
    const albumList = [
        {id: 1, name: "2023_01", count: 4, photo: User01c},
        {id: 2, name: "2023_02", count: 10, photo: User02c},
        {id: 3, name: "2023_03", count: 25, photo: User03c},
        {id: 4, name: "2023_04", count: 1, photo: User04c}
    ]
    
    const navigate = useNavigate();
               
    function handleClick() {
        navigate("/");
    }

    return (
        <div>
            <header className="border-bottom border-light border-5 mb-5 p-2" style={{"margin-top": "-70px"}}>
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
                        {
                            albumList.map(
                                albumList => (
                                    <tr key={albumList.id}>
                                        <td>{albumList.name}</td>
                                        <td>{albumList.count}</td>
                                        <td onClick={handleClick} style={{ cursor: 'pointer' }}><img src={albumList.photo} alt="user photo" /></td>
                                    </tr>
                                )
                            )
                        }
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    )   
}