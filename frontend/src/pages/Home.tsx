import {useNavigate} from "react-router";
import styles from "../assets/styles/home.module.css";
import {CustomLayout} from "../components";

function Home() {
    const navigate = useNavigate(); // ← Hook pour naviguer

    return (
        <CustomLayout>
            <div className={styles.homePage}>
                <p>a remplir description</p>
                <button onClick={() => navigate("/compute")} className={styles.button}>
                    Commencer à utiliser EcoTrip
                </button>
            </div>
        </CustomLayout>
    )
}

export default Home