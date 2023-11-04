import { useState } from 'react'
import { BrowserRouter, Routes, Route, useNavigate } from 'react-router-dom';
import './PhotoAlbum.css'

export default function PhotoAlbum() {
    return (
        <div className="PhotoAlbum">
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<LoginComponent />}></Route>
                    <Route path="/login" element={<LoginComponent />}></Route>
                    <Route path="/welcome" element={<WelcomeComponent />}></Route>
                    <Route path="*" element={<ErrorComponent />}></Route>
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
            navigate('/welcome');
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
                    <input type="text" placeholder="이메일 입력" name="email" value={email} onChange={handleEmailChange}/>
                </div>
                <div>
                    <input type="password" placeholder="비밀번호 입력" name="password" value={password} onChange={handlePasswordChange}/>
                </div>
                <div>
                    <button type="button" name="login" onClick={handleSubmit}>로그인</button>
                </div>
            </div>
        </div>
    )
}

function WelcomeComponent() {
    return (
        <div className="Welcome">
            <h1>환영합니다!</h1>
            <div>Welcome Component</div> 
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