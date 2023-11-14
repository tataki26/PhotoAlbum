import { useNavigate, useParams } from 'react-router-dom';

export default function WelcomeComponent() {
    const {username} = useParams(); // {username} == params.username
    
    const navigate = useNavigate();

    function handleMoveToAlbumClick() {
        navigate("/albums");
    }

    return (
        <div className="WelcomeComponent">
            <label className='welcomeMessage'>환영합니다, {username}!</label>
            <div>
                <button className="WelcomeButton" type="button" aria-label="앨범 페이지로 이동" onClick={handleMoveToAlbumClick}>내 앨범으로 이동</button>
            </div>
        </div>
    )
}