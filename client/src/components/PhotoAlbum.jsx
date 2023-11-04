import { useState } from 'react'
import './PhotoAlbum.css'

export default function PhotoAlbum() {
    return (
        <div className="PhotoAlbum">
            <LoginComponent />
        </div>
    )
}

function LoginComponent() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [showErrorMessage, setShowErrorMessage] = useState(false);

    function handleEmailChange(e) {
        setEmail(e.target.value);
    }

    function handlePasswordChange(e) {
        setPassword(e.target.value);
    }

    function handleSubmit() {
        if (email !== "abc@test.com" || password !== "1234") {
            setShowErrorMessage(true);
        }
        else {
            setShowErrorMessage(false);
        }
    }

    return (
        <div className="Login">
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