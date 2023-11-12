import { createContext, useContext, useState } from "react";

export const AuthContext = createContext();
export const useAuth = () => useContext(AuthContext);

export default function AuthProvider({ children }) {
    const [isAuthenticated, setAuthenticated] = useState(false);
    return (
        <AuthContext.Provider value={ {isAuthenticated, setAuthenticated} }>
            {children}
        </AuthContext.Provider>
    )
}