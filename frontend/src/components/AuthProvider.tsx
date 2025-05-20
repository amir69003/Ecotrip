import React, {useEffect, useState} from 'react';
import {useCookies} from 'react-cookie';
import {AuthUser} from "../model";
import {AuthContext} from '../lib/auth.ts';

const AuthProvider = ({children}: { children: React.ReactNode }) => {
    const [cookies] = useCookies(['access_token']);
    const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
    const [user, setUser] = useState<AuthUser | null>(null);
    const contextValue = React.useMemo(() => ({
        isAuthenticated,
        setIsAuthenticated,
        user,
        setUser
    }), [isAuthenticated, user]);

    useEffect(() => {
        const username = localStorage.getItem("username");
        const email = localStorage.getItem("email");
        const roles = localStorage.getItem("roles")?.split(',');

        if ('access_token' in cookies && username && email && roles != undefined) {
            setIsAuthenticated(true);
            setUser({username, email, roles});
        } else {
            setIsAuthenticated(false);
            setUser(null);
            localStorage.removeItem("username");
            localStorage.removeItem("email");
            localStorage.removeItem("roles");
        }
    }, [cookies, cookies.access_token]);

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;