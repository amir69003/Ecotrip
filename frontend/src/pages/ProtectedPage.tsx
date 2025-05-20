import React, {JSX, useEffect} from "react";
import {useLocation, useNavigate} from "react-router";
import {useAuthSession} from "../api/auth";

type ProtectedPageProps = {
    children: JSX.Element;
}

const ProtectedPage: React.FC<ProtectedPageProps> = ({children}) => {
    const {isAuthenticated} = useAuthSession();
    const location = useLocation();
    const navigate = useNavigate();
    const current = location.pathname;

    useEffect(() => {
        if (isAuthenticated == false) {
            navigate("/login", {
                state: {from: current},
                replace: true,
            });
        }
    }, [isAuthenticated, navigate, current]);

    if (!isAuthenticated) {
        return null;
    }

    return (children);
}

export default ProtectedPage;