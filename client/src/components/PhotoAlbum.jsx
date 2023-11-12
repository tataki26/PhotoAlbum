import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './security/AuthContext';
import RootComponent from './RootComponent';
import LoginComponent from './LoginComponent';
import LogoutComponent from './LogoutComponent';
import UserComponent from './UserComponent';
import WelcomeComponent from './WelcomeComponent';
import HeaderComponent from './HeaderComponent';
import ListAlbumComponent from './ListAlbumComponent';
import ErrorComponent from './ErrorComponent';
import AuthProvider from './security/AuthContext';
import './PhotoAlbum.css'

function AuthenticatedRoute({ children }) {
    const authContext = useAuth();
    
    if (authContext.isAuthenticated)
        return children;

    return <Navigate to="/login" />
}

export default function PhotoAlbum() {
    return (
        <div className="PhotoAlbum">
            <AuthProvider>
                <BrowserRouter>
                    <HeaderComponent />
                    <Routes>
                        <Route path="/" element={<RootComponent />} />
                        <Route path="/login" element={<LoginComponent />} />
                        <Route path="/logout" element={
                            <AuthenticatedRoute>
                                <LogoutComponent />
                            </AuthenticatedRoute>
                        } />
                        <Route path="/users" element={<UserComponent />} />
                        <Route path="/welcome/:username" element={
                            <AuthenticatedRoute>
                                <WelcomeComponent />
                            </AuthenticatedRoute>
                        } />
                        <Route path="/albums" element={
                            <AuthenticatedRoute>
                                <ListAlbumComponent />
                            </AuthenticatedRoute>
                        } />
                        <Route path="*" element={<ErrorComponent />} />
                    </Routes>
                </BrowserRouter>
            </AuthProvider>
        </div>
    )
}