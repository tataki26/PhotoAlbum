import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom';

export default function LogoutComponent() {
    const navigate = useNavigate()

    useEffect(() => {
        navigate("/");
    }, []);

    return null;
}