import EcoTrip from "./../assets/images/EcoTrip.png";
import styles from "./../assets/styles/home.module.css";
import LoginForm from "../components/LoginForm.tsx";
import {useLoginForm} from "../hooks/useLoginForm.ts";
import { useNavigate } from "react-router-dom";

/* #TODO: Appel a l'API pour se connecter */


function Home() {
    const navigate = useNavigate(); // ← Hook pour naviguer

    const { loginData, handleChange, handleSubmit } = useLoginForm((data) => {
        alert("La connexion se fait avec l'email : " + data.email + " et le mot de passe " + data.password);
        // 🔜 Ici, tu feras ton appel API réel
        navigate("/calcul");
    });


    return (
        <>
            <div className={styles.homePage}>
                <div className={styles.leftHomePage}>
                    <h1 className={styles.title}>
                        Bienvenue sur
                    </h1>
                    <img src={EcoTrip} className={styles.logo} alt="Vite logo" />
                    <button className={styles.button} onClick={()=> navigate("/register")}>Rejoignez-nous !</button>
                </div>
                <div className={styles.rightHomePage}>
                    <LoginForm loginData={loginData} handleChange={handleChange} handleSubmit={handleSubmit} />
                </div>
            </div>
        </>
    )
}
export default Home