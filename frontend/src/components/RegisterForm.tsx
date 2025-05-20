import * as React from "react";
import styles from "../assets/styles/register.module.css";
import {LockKeyhole, Mail, User} from "lucide-react";
import {RegisterDTO} from "../model";

type RegisterFormProps = {
    isLoading: boolean;
    error: Error | null;
    formData: RegisterDTO;
    handleChange: React.ChangeEventHandler<HTMLInputElement>;
    handleSubmit: React.FormEventHandler<HTMLFormElement>;
}

const RegisterForm: React.FC<RegisterFormProps> = ({isLoading, error, formData, handleChange, handleSubmit}) => {

    if (isLoading) {
        return (
            <div className={styles.loadingContainer}>
                <h1 className={styles.title}>Loading...</h1>
            </div>
        )
    }

    return (
        <form onSubmit={handleSubmit}>
            <h1 className={styles.title}>Inscription</h1>
            {error && <p className={styles.error}>{error.message}</p>}
            <div>
                <h2 className={styles.info}>Username</h2>
                <div className={styles.inputContainer}>
                    <User/>
                    <input
                        type="username"
                        id="username"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        className={styles.input}
                        required
                    />
                </div>
            </div>
            <div>
                <h2 className={styles.info}>Email</h2>
                <div className={styles.inputContainer}>
                    <Mail/>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        className={styles.input}
                        required
                    />
                </div>
            </div>
            <div>
                <h2 className={styles.info}>Mot de passe</h2>
                <div className={styles.inputContainer}>
                    <LockKeyhole/>
                    <input
                        className={styles.input}
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>
            </div>
            <div>
                <h2 className={styles.info}>Confirmer le Mot de passe</h2>
                <div className={styles.inputContainer}>
                    <LockKeyhole/>
                    <input
                        className={styles.input}
                        type="password"
                        id="confirmPassword"
                        name="confirmPassword"
                        value={formData.confirmPassword}
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