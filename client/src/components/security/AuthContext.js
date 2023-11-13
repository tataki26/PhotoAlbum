import { createContext, useContext, useState } from "react";
import { apiClient } from "../../apis/ApiClient";
import { loginApi } from "../../apis/MemberApiService";

export const AuthContext = createContext();
export const useAuth = () => useContext(AuthContext);

export default function AuthProvider({ children }) {
    const [isAuthenticated, setAuthenticated] = useState(false);
    const [token, setToken] = useState(null);

    async function login(email, password) {
        try {
            const member = {
                email: email,
                password: password
            };

            // wait for response - sync
            const response = await loginApi(member);

            // eslint-disable-next-line
            if (response.status == 200) {
                const jwtToken = 'Bearer ' + response.data.token;
                
                setAuthenticated(true);
                setToken(jwtToken);

                apiClient.interceptors.request.use(
                    (config) => {
                        console.log('intercepting and adding a token');
                        config.headers.Authorization=jwtToken;
                        return config;
                    }
                )
                return true;
            } else {
                logout();
                return false;
            }
        } catch (error) {
            logout();
            return false;
        }
    }

    function logout() {
        setAuthenticated(false);
        setToken(null);
    }

    return (
        <AuthContext.Provider value={ {isAuthenticated, token, login, logout} }>
            {children}
        </AuthContext.Provider>
    )
}