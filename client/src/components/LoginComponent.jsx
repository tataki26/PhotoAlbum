import { useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { useAuth } from './security/AuthContext';

export default function LoginComponent() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [showErrorMessage, setShowErrorMessage] = useState(false);

    const navigate = useNavigate();

    const authContext = useAuth();

    function handleEmailChange(e) {
        setEmail(e.target.value);
    }

    function handlePasswordChange(e) {
        setPassword(e.target.value);
    }

    async function handleSubmit() {
        if (await authContext.login(email, password)) {
            const atIndex = email.indexOf("@");
            const username = email.slice(0, atIndex);
            navigate(`/welcome/${username}`);
        }
        else {
            setShowErrorMessage(true);
        }
    }

    return (
        <div className="LoginComponent">
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