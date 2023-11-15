import vector5 from '../assets/svgs/vector5.svg';
import downArrow from '../assets/svgs/down-arrow.svg';

export default function ListAlbumComponent() {
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
        </div>
    )   
}