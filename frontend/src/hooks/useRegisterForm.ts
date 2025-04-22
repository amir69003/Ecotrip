import {useState} from "react";
import * as React from "react";

type RegisterData = {
    email: string,
    username: string,
    password: string,
    confirmPassword: string,
}
export function useRegisterForm( onSubmit: (data :RegisterData) => void) {
    const [loginData, setLoginData] = useState<RegisterData>({
        email:'',
        username:'',
        password:'',
        confirmPassword:''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name , value } = e.target;
        setLoginData(prevData => ({...prevData, [name]: value}));
    }


    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        if (loginData.password !== loginData.confirmPassword) {
            alert("Les mots de passe ne correspondent pas !");
            return;
        }
        onSubmit(loginData);
    }

    return {loginData, handleChange, handleSubmit};
}