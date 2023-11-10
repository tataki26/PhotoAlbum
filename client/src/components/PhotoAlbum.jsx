import { useState } from 'react'
import { BrowserRouter, Routes, Route, useNavigate, useParams, Link } from 'react-router-dom';
import './PhotoAlbum.css'

export default function PhotoAlbum() {
    return (
        <div className="PhotoAlbum">
            <BrowserRouter>
                <HeaderComponent />
                <Routes>
                    <Route path="/" element={<LoginComponent />} />
                    <Route path="/login" element={<LoginComponent />} />
                    <Route path="/welcome/:username" element={<WelcomeComponent />} />
                    <Route path="/albums" element={<ListAlbumComponent />} />

                    <Route path="*" element={<ErrorComponent />} />
                </Routes>
            </BrowserRouter>
        </div>
    )
}

function LoginComponent() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [showErrorMessage, setShowErrorMessage] = useState(false);

    const navigate = useNavigate();

    function handleEmailChange(e) {
        setEmail(e.target.value);
    }

    function handlePasswordChange(e) {
        setPassword(e.target.value);
    }

    function handleSubmit() {
        if (email === "abc@test.com" && password === "1234") {
            setShowErrorMessage(false);
            const atIndex = email.indexOf("@");
            const username = email.slice(0, atIndex);
            navigate(`/welcome/${username}`);
        }
        else {
            setShowErrorMessage(true);
        }
    }

    return (
        <div className="Login">
            <h1>로그인</h1>
            {showErrorMessage && <div className="errorMessage">아이디 또는 비밀번호가 일치하지 않습니다</div>}
            <div className="LoginForm">
                <div>
                    <input className="LoginInput" type="text" placeholder="이메일 입력" name="email" value={email} onChange={handleEmailChange}/>
                </div>
                <div>
                    <input className="LoginInput" type="password" placeholder="비밀번호 입력" name="password" value={password} onChange={handlePasswordChange}/>
                </div>
                <div>
                    <button className="LoginButton" type="button" name="login" onClick={handleSubmit}>로그인</button>
                </div>
            </div>
        </div>
    )
}

function WelcomeComponent() {
    const {username} = useParams(); // {username} == params.username
    
    const navigate = useNavigate();

    function handleMoveToAlbumClick() {
        navigate("/albums");
    }

    return (
        <div className="WelcomeComponent">
            <h1>환영합니다, {username}!</h1>
            <div>
                <button className="WelcomeButton" type="button" aria-label="앨범 페이지로 이동" onClick={handleMoveToAlbumClick}>내 앨범으로 이동</button>
            </div>
        </div>
    )
}

function ErrorComponent() {
    return (
        <div className="ErrorComponent">
            <h1>** 존재하지 않는 페이지입니다 **</h1>
            <div>
                404 에러가 발생했습니다<br />
                123-456으로 문의 바랍니다
            </div>
        </div>
    )
}

function ListAlbumComponent() {
    return (
        <div className="ListAlbumComponent">
            <h1>내 사진첩</h1>
            <div>
                Album Details
            </div>
        </div>
    )
}

function HeaderComponent() {
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
                        </ul>
                    </nav>
                </div>
            </div>
        </header>
    )
}
