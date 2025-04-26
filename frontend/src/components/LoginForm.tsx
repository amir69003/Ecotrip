import * as React from "react";
import styles from "../assets/styles/home.module.css";
import {LockKeyhole, User} from "lucide-react";

type LoginFormProps = {
    loginData : { email: string, password: string };
    handleChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    handleSubmit: (e: React.FormEvent) => void;

}

const LoginForm: React.FC<LoginFormProps> = ({ loginData , handleChange, handleSubmit}) =>{
    return (
        <form onSubmit={handleSubmit}>
            <h1 className={styles.title}>Connexion</h1>
            <div>
                <h2 className={styles.info}>Email</h2>
                <div className={styles.inputContainer}>
                    <User />
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={loginData.email}
                        onChange={handleChange}
                        className={styles.input}
                        required
                    />
                </div>
            </div>
            <div>
                <h2 className={styles.info}>Mots de passe</h2>
                <div className={styles.inputContainer}>
                    <LockKeyhole />
                    <input
                        className={styles.input}
                        type="password"
                        id="password"
                        name="password"
                        value={loginData.password}
                        onChange={handleChange}
                        required
                    />
                </div>
            </div>
            <button className={styles.submitButton} type="submit">Connexion</button>
        </form>
    )
}
export default LoginForm;