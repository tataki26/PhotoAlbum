import { Link } from 'react-router-dom';
import { useAuth } from './security/AuthContext';
import logo from '../assets/pngs/ph_diamond.png';

export default function HeaderComponent() {
    const authContext = useAuth();
    const isAuthenticated = authContext.isAuthenticated;

    return (
        <header className="border-bottom border-light border-5 mb-5 p-2">
            <div className="container">
                <div className="row">
                    <nav className="navbar navbar-expand-lg">
                        <Link to="http://localhost:3000" className="LogoImg2">
                            <img src={logo} alt="logo" />
                        </Link>
                        <Link id="customNavbarLink" className="navbar-brand ms-2 fs-2" style={{color: "#A44A21"}} to="http://localhost:3000">PhotoAlbum</Link>
                        <div className="collapse navbar-collapse">
                            <ul className="navbar-nav">
                                <li className="nav-item fs-5 mt-2">
                                    {isAuthenticated
                                        && <Link className="nav-link" to="/welcome/abc">Home</Link>}
                                </li>
                                <li className="nav-item fs-5 mt-2">
                                    {isAuthenticated
                                        && <Link className="nav-link" to="/albums">Albums</Link>}
                                </li>
                            </ul>
                        </div>
                        <ul className="navbar-nav">
                            <li className="nav-item fs-5 mt-2">
                                {!isAuthenticated
                                    &&<Link className="nav-link" to="/login">Login</Link>}
                            </li>
                            <li className="nav-item fs-5 mt-2">
                                {isAuthenticated
                                    && <Link className="nav-link" to="/logout" onClick={() => authContext.logout()}>Logout</Link>}
                            </li>
                            <li className="nav-item fs-5 mt-2">
                                {!isAuthenticated
                                    &&<Link className="nav-link" to="/users">Join Us</Link>}
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </header>
    )
}