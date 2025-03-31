import EcoTrip from "./../assets/images/EcoTrip.png";
import styles from "./../assets/styles/home.module.css";
import * as React from "react";
import {useState} from "react";
import LoginForm from "../components/LoginForm.tsx";

/* #TODO: Appel a l'API pour se connecter */

type LoginData = {
    email: string,
    password: string,
}
function Home() {
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
        alert("La connexion se fait avec l'email : "+ loginData.email+" et le mot de passe "+ loginData.password);
    }
    return (
        <>
            <div className={styles.homePage}>
                <div className={styles.leftHomePage}>
                    <h1 className={styles.title}>
                        Bienvenue sur
                    </h1>
                    <img src={EcoTrip} className={styles.logo} alt="Vite logo" />
                    <button className={styles.button} onClick={()=> alert("Le user veut crée un compte !")}>Rejoignez-nous !</button>
                </div>
                <div className={styles.rightHomePage}>
                    <LoginForm loginData={loginData} handleChange={handleChange} handleSubmit={handleSubmit} />
                </div>
            </div>
        </>
    )
}
export default Home