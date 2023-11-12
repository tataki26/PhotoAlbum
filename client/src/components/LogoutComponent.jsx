import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom';
import { useAuth } from './security/AuthContext';

export default function LogoutComponent() {
    const navigate = useNavigate();
    const authContext = useAuth();

    useEffect(() => {
        navigate("/");
        authContext.logout();
        // eslint-disable-next-line
    }, []);

    return null;
}