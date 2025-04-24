import * as React from "react";
import styles from "../assets/styles/register.module.css";
import {LockKeyhole, Mail ,User} from "lucide-react";

type RegisterFormProps = {
    registerData : { email: string, username: string, password: string, confirmPassword: string };
    handleChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    handleSubmit: (e: React.FormEvent) => void;

}

const RegisterForm: React.FC<RegisterFormProps> = ({ registerData , handleChange, handleSubmit}) =>{
    return (
        <form onSubmit={handleSubmit}>
            <h1 className={styles.title}>Inscription</h1>
            <div>
                <h2 className={styles.info}>Username</h2>
                <div className={styles.inputContainer}>
                    <User />
                    <input
                        type="username"
                        id="username"
                        name="username"
                        value={registerData.username}
                        onChange={handleChange}
                        className={styles.input}
                        required
                    />
                </div>
            </div>
            <div>
                <h2 className={styles.info}>Email</h2>
                <div className={styles.inputContainer}>
                    <Mail />
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={registerData.email}
                        onChange={handleChange}
                        className={styles.input}
                        required
                    />
                </div>
            </div>
            <div>
                <h2 className={styles.info}>Mot de passe</h2>
                <div className={styles.inputContainer}>
                    <LockKeyhole />
                    <input
                        className={styles.input}
                        type="password"
                        id="password"
                        name="password"
                        value={registerData.password}
                        onChange={handleChange}
                        required
                    />
                </div>
            </div>
            <div>
                <h2 className={styles.info}>Confirmer le Mot de passe</h2>
                <div className={styles.inputContainer}>
                    <LockKeyhole />
                    <input
                        className={styles.input}
                        type="password"
                        id="confirmPassword"
                        name="confirmPassword"
                        value={registerData.confirmPassword}
                        onChange={handleChange}
                        required
                    />
                </div>
            </div>
            <button className={styles.submitButton} type="submit">Crée mon compte</button>
        </form>
    )
}
export default RegisterForm;