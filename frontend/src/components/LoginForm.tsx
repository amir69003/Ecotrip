import * as React from "react";
import styles from "../assets/styles/login.module.css";
import {LockKeyhole, User} from "lucide-react";
import {LoginDTO} from "../model";

type LoginFormProps = {
    isLoading: boolean;
    error: Error | null;
    formData: LoginDTO;
    handleChange: React.ChangeEventHandler<HTMLInputElement>;
    handleSubmit: React.FormEventHandler<HTMLFormElement>;
}

const LoginForm: React.FC<LoginFormProps> = ({isLoading, error, formData, handleChange, handleSubmit}) => {

    if (isLoading) {
        return (
            <div className={styles.loadingContainer}>
                <h1 className={styles.title}>Loading...</h1>
            </div>
        )
    }

    return (
        <form onSubmit={handleSubmit}>
            <h1 className={styles.title}>Connexion</h1>
            {error && <p className={styles.error}>{error.message}</p>}
            <div>
                <h2 className={styles.info}>Email</h2>
                <div className={styles.inputContainer}>
                    <User/>
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
                <h2 className={styles.info}>Mots de passe</h2>
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
            <button className={styles.submitButton} type="submit">Connexion</button>
        </form>
    )
}

export default LoginForm;