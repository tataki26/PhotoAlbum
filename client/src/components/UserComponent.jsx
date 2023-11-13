import { useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { createMemberApi } from '../apis/MemberApiService';

export default function UserComponent() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

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

    function handleConfirmPasswordChange(e) {
        setConfirmPassword(e.target.value);
    }

    function handleSubmit() {
        if (password !== confirmPassword) {
            alert('비밀번호가 일치하지 않습니다');
            return;
        }

        const member = {
            memberName: username,
            email: email,
            password: password
        };

        console.log(member);

        createMemberApi(member)
        .then(response => {
            console.log(response);
            alert('회원 가입이 완료되었습니다');
            navigate("/");
        })
        .catch(error => console.log(error));
    }

    return (
        <div className="UserComponent">
            <h1>회원 가입</h1>
            <div className="UserForm">
                <div>
                    <input className="UserInput" type="text" placeholder="이름 입력" name="username" value={username} onChange={handleUsernameChange}/>
                </div>
                <div>
                    <input className="UserInput" type="text" placeholder="이메일 입력" name="email" value={email} onChange={handleEmailChange}/>
                </div>
                <div>
                    <input className="UserInput" type="password" placeholder="비밀번호 입력" name="password" value={password} onChange={handlePasswordChange}/>
                </div>
                <div>
                    <input className="UserInput" type="password" placeholder="비밀번호 재입력" name="confirmPassword" value={confirmPassword} onChange={handleConfirmPasswordChange}/>
                </div>
                <div>
                    <button className="UserButton" type="button" aria-label="회원 가입" onClick={handleSubmit}>회원 가입</button>
                </div>
            </div>
        </div>
    )
}