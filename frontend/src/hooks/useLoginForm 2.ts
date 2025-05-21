import {useState} from "react";
import * as React from "react";

type LoginData = {
    email: string,
    password: string,
}
export function useLoginForm( onSubmit: (data :LoginData) => void) {
    const [loginData, setLoginData] = useState<LoginData>({
        email:'',
        password:''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name , value } = e.target;
        setLoginData(prevData => ({...prevData, [name]: value}));
    }


    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        onSubmit(loginData);
    }

    return {loginData, handleChange, handleSubmit};
}