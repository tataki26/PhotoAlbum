import { Link } from 'react-router-dom';

export default function HeaderComponent() {
    return (
        <header className="border-bottom border-light border-5 mb-5 p-2">
            <div className="container">
                <div className="row">
                    <nav className="navbar navbar-expand-lg">
                        <Link className="navbar-brand ms-2 fs-2 fw-bold text-black" to="http://localhost:3000">PhotoAlbum</Link>
                        <div className="collapse navbar-collapse">
                            <ul className="navbar-nav">
                                <li className="nav-item fs-5"><Link className="nav-link" to="/welcome/abc">Home</Link></li>
                                <li className="nav-item fs-5"><Link className="nav-link" to="/albums">Albums</Link></li>
                            </ul>
                        </div>
                        <ul className="navbar-nav">
                            <li className="nav-item fs-5"><Link className="nav-link" to="/login">Login</Link></li>
                            <li className="nav-item fs-5"><Link className="nav-link" to="/logout">Logout</Link></li>
                            <li className="nav-item fs-5"><Link className="nav-link" to="/users">Join Us</Link></li>
                        </ul>
                    </nav>
                </div>
            </div>
        </header>
    )
}