import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginComponent from './LoginComponent';
import LogoutComponent from './LogoutComponent';
import UserComponent from './UserComponent';
import WelcomeComponent from './WelcomeComponent';
import HeaderComponent from './HeaderComponent';
import ListAlbumComponent from './ListAlbumComponent';
import ErrorComponent from './ErrorComponent';
import AuthProvider from './security/AuthContext';
import './PhotoAlbum.css'

export default function PhotoAlbum() {
    return (
        <div className="PhotoAlbum">
            <AuthProvider>
                <BrowserRouter>
                    <HeaderComponent />
                    <Routes>
                        <Route path="/" element={<LoginComponent />} />
                        <Route path="/login" element={<LoginComponent />} />
                        <Route path="/logout" element={<LogoutComponent />} />
                        <Route path="/users" element={<UserComponent />} />
                        <Route path="/welcome/:username" element={<WelcomeComponent />} />
                        <Route path="/albums" element={<ListAlbumComponent />} />
                        <Route path="*" element={<ErrorComponent />} />
                    </Routes>
                </BrowserRouter>
            </AuthProvider>
        </div>
    )
}