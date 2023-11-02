export default function PhotoAlbum() {
    return (
        <div className="PhotoAlbum">
            Photo Album Application
            <LoginComponent />
        </div>
    )
}

function LoginComponent() {
    return (
        <div className="Login">
            <div className="LoginForm">
                <div>
                    <input type="text" placeholder="이메일 입력" name="email" />
                </div>
                <div>
                    <input type="password" placeholder="비밀번호 입력" name="password" />
                </div>
                <div>
                    <button type="button" name="login">로그인</button>
                </div>
            </div>
        </div>
    )
}