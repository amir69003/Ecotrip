import {useMutation} from '@tanstack/react-query'
import {AuthContextType, AuthUser, LoginDTO, RegisterDTO} from "../model";
import {createContext, useContext} from "react";
import {useCookies} from "react-cookie";

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useLogin = () => {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useLogin must be used within AuthProvider");

    return useMutation({
        mutationFn: async ({email, password}: LoginDTO) => {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                credentials: 'include',
                body: JSON.stringify({email, password}),
            });
            if (!response.ok) throw new Error('Login failed');
            return await response.json() as AuthUser;
        },
        onSuccess: (data) => {
            ctx.setIsAuthenticated(true);
            localStorage.setItem("username", data.username);
            localStorage.setItem("email", data.email);
            localStorage.setItem("roles", data.roles.join(','));
            ctx.setUser(data);
        }
    });
}

export const useLogout = () => {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useLogout must be used within AuthProvider");
    const [, , removeCookie] = useCookies(['access_token']);

    return () => {
        removeCookie('access_token');
        localStorage.removeItem("username");
        localStorage.removeItem("email");
        localStorage.removeItem("roles");
        ctx.setIsAuthenticated(false);
        ctx.setUser(null);
    }
}

export const useRegister = () => {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useRegister must be used within AuthProvider");

    return useMutation({
        mutationFn: async ({username, email, password, confirmPassword}: RegisterDTO) => {
            if (password !== confirmPassword) throw new Error('Passwords do not match');
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                credentials: 'include',
                body: JSON.stringify({username, email, password}),
            });
            if (!response.ok) throw new Error('Registration failed');
            return await response.json() as AuthUser;
        },
        onSuccess: (data) => {
            ctx.setIsAuthenticated(true);
            localStorage.setItem("username", data.username);
            localStorage.setItem("email", data.email);
            localStorage.setItem("roles", data.roles.join(','));
            ctx.setUser(data);
        }
    });
}

export const useAuthSession = () => {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useAuthSession must be used within AuthProvider");
    return {isAuthenticated: ctx.isAuthenticated, user: ctx.user};
}