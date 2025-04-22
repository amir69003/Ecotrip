import EcoTrip from "./../assets/images/EcoTrip.png";
import styles from "./../assets/styles/register.module.css";
import { useNavigate } from "react-router-dom";
import RegisterForm from "../components/RegisterForm.tsx";
import {useRegisterForm} from "../hooks/useRegisterForm.ts";

/* #TODO: Appel a l'API pour se connecter */


function RegisterPage() {
    const navigate = useNavigate(); // ← Hook pour naviguer

    const { loginData, handleChange, handleSubmit } = useRegisterForm((data) => {
        alert("La connexion se fait avec l'email : " + data.email + " et le mot de passe " + data.password);
        // 🔜 Ici, tu feras ton appel API réel
        navigate("/");
    });


    return (
        <>
            <div className={styles.homePage}>
                <div className={styles.leftHomePage}>
                    <RegisterForm registerData={loginData} handleChange={handleChange} handleSubmit={handleSubmit} />
                </div>
                <div className={styles.rightHomePage}>
                    <h1 className={styles.title}>
                        Déja un compte ?
                    </h1>
                    <img src={EcoTrip} className={styles.logo} alt="Vite logo" />
                    <button className={styles.button} onClick={()=> navigate("/")}>Connectez-vous !</button>
                </div>

            </div>
        </>
    )
}
export default RegisterPage