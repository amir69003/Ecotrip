import { UserCircle } from "lucide-react"; // Import de l'icône
import styles from "./../assets/styles/header.module.css";
import EcoTrip from "../assets/images/EcoTrip.png";
import * as React from "react";


type HeaderProps = {
    isAuthenticated: boolean;
}
const Header: React.FC<HeaderProps> = ({isAuthenticated}) => {
    return (
        <div className={styles.header}>
            <img src={EcoTrip} className={styles.logo} alt="EcoTrip logo" />
            <div className={styles.buttonContainer}>
                {isAuthenticated ? (
                    <UserCircle size={40} className={styles.avatarIcon} />
                ) : (
                    <button className={styles.button} onClick={() => alert("Le user veut se connecter !")}>
                        Connexion
                    </button>
                )}
            </div>
        </div>
    );
}

export default Header;