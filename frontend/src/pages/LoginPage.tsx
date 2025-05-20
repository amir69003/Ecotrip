import EcoTrip from "./../assets/images/EcoTrip.png";
import styles from "../assets/styles/login.module.css";
import LoginForm from "../components/LoginForm";
import {useLocation, useNavigate} from "react-router";
import {useLogin} from "../lib/auth";
import React, {useEffect, useState} from "react";
import {LoginDTO} from "../model";

const LoginPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const from = location.state.from ?? "/";
    const login = useLogin();

    const [formData, setFormData] = useState<LoginDTO>({
        email: '',
        password: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData(prevData => ({...prevData, [name]: value}));
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        login.mutate(formData);
    }

    useEffect(() => {
        if (login.isSuccess) {
            navigate(from, {replace: true});
        }
    }, [login.isSuccess, navigate, from]);

    return (
        <div className={styles.homePage}>
            <div className={styles.leftHomePage}>
                <h1 className={styles.title}>
                    Bienvenue sur
                </h1>
                <img src={EcoTrip} className={styles.logo} alt="Vite logo"/>
                <button className={styles.button} onClick={() => navigate("/register", {state: {from}})}>
                    Rejoignez-nous !
                </button>
            </div>
            <div className={styles.rightHomePage}>
                <LoginForm
                    isLoading={login.isPending}
                    error={login.error}
                    formData={formData}
                    handleChange={handleChange}
                    handleSubmit={handleSubmit}
                />
            </div>
        </div>
    )
}

export default LoginPage