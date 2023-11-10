import { useState } from 'react'
import { useNavigate } from 'react-router-dom';

export default function UserComponent() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    function handleUsernameChange(e) {
        setUsername(e.target.value);
    }

    function handleEmailChange(e) {
        setEmail(e.target.value);
    }

    function handlePasswordChange(e) {
        setPassword(e.target.value);
    }

    function handleSubmit() {
        navigate(`/welcome/${username}`);
    }

    return (
        <div className="UserComponent">
            <h1>회원 가입</h1>
            <div className="UserForm">
                <div>
                    <input className="UserInput" type="text" placeholder="이름 입력" name="email" value={username} onChange={handleUsernameChange}/>
                </div>
                <div>
                    <input className="UserInput" type="text" placeholder="이메일 입력" name="password" value={email} onChange={handleEmailChange}/>
                </div>
                <div>
                    <input className="UserInput" type="password" placeholder="비밀번호 입력" name="password" value={password} onChange={handlePasswordChange}/>
                </div>
                <div>
                    <input className="UserInput" type="password" placeholder="비밀번호 재입력" name="password" value={password} onChange={handlePasswordChange}/>
                </div>
                <div>
                    <button className="UserButton" type="button" aria-label="회원 가입" onClick={handleSubmit}>회원 가입</button>
                </div>
            </div>
        </div>
    )
}