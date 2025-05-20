import {UserCircle} from "lucide-react"; // Import de l'icône
import styles from "./../assets/styles/header.module.css";
import EcoTrip from "../assets/images/EcoTrip.png";
import {useLocation, useNavigate} from "react-router";
import {useAuthSession, useLogout} from "../lib/auth";

const Header = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const current = location.pathname;

    const {isAuthenticated, user} = useAuthSession();
    const logout = useLogout();

    return (
        <div className={styles.header}>
            <div className={styles.menuContainer}>
                {current != "/history" && (
                    <button className={styles.button} onClick={() => navigate("/history")}>
                        Historique trajets
                    </button>
                )}
            </div>
            <img src={EcoTrip} className={styles.logo} alt="EcoTrip logo" onClick={() => {
                navigate("/");
            }}/>
            <div className={styles.buttonContainer}>
                {isAuthenticated ? (
                    <>
                        <UserCircle size={40} className={styles.avatarIcon}/>
                        <span className={styles.userName}>{user?.username}</span>
                        <button className={styles.deleteButton} onClick={() => logout()}>
                            Déconnexion
                        </button>
                    </>
                ) : (
                    <button className={styles.button} onClick={() => navigate("/login", {state: {from: current}})}>
                        Connexion
                    </button>
                )}
            </div>
        </div>
    );
}

export default Header;