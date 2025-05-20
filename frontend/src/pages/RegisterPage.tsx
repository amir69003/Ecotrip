import React, {useEffect, useState} from "react";
import EcoTrip from "./../assets/images/EcoTrip.png";
import styles from "./../assets/styles/register.module.css";
import {useLocation, useNavigate} from "react-router";
import RegisterForm from "../components/RegisterForm";
import {useRegister} from "../lib/auth";
import {RegisterDTO} from "../model";

const RegisterPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const from = location.state.from ?? "/";
    const register = useRegister();

    const [formData, setFormData] = useState<RegisterDTO>({
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData(prevData => ({...prevData, [name]: value}));
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        register.mutate(formData);
    }

    useEffect(() => {
        if (register.isSuccess) {
            navigate(from, {replace: true});
        }
    }, [register.isSuccess, navigate, from]);

    return (
        <div className={styles.homePage}>
            <div className={styles.leftHomePage}>
                <RegisterForm
                    isLoading={register.isPending}
                    error={register.error}
                    formData={formData}
                    handleChange={handleChange}
                    handleSubmit={handleSubmit}
                />
            </div>
            <div className={styles.rightHomePage}>
                <h1 className={styles.title}>
                    Déja un compte ?
                </h1>
                <img src={EcoTrip} className={styles.logo} alt="Vite logo"/>
                <button className={styles.button} onClick={() => navigate("/login", {state: {from}})}>
                    Connectez-vous !
                </button>
            </div>

        </div>
    )
}

export default RegisterPage