import styles from "../assets/styles/result.module.css";
import Header from "../components/Header.tsx";
import ItineraryMap from "../components/ItineraryMap.tsx";
import { useLocation, useNavigate } from "react-router-dom";

type CarbonData = {
    departure: string;
    arrival: string;
    transport: string;
    carbon_impact: number;
};

function ResultPage() {
    const location = useLocation();
    const navigate = useNavigate();

    const data = location.state as CarbonData;

    // fallback si aucun trajet n’est passé (accès direct à /result)
    if (!data) {
        return (
            <div className={styles.resultPage}>
                <Header isAuthenticated={true} />
                <div className={styles.whiteContainer}>
                    <h2>Aucun trajet sélectionné.</h2>
                    <button className={styles.button} onClick={() => navigate("/history")}>
                        Retour à l'historique
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className={styles.resultPage}>
            <Header isAuthenticated={true} />
            <div className={styles.whiteContainer}>
                <div className={styles.mapResultContainer}>
                    <div className={styles.map}>
                        <ItineraryMap departure={data.departure} arrival={data.arrival} />
                    </div>
                    <div className={styles.resultContainer}>
                        <div className={styles.collumData}>
                            <h2>Départ</h2>
                            <h1>{data.departure}</h1>
                        </div>
                        <div className={styles.collumData}>
                            <h2>Arrivé</h2>
                            <h1>{data.arrival}</h1>
                        </div>
                        <div className={styles.collumData}>
                            <h2>Transport utilisé</h2>
                            <h1>{data.transport}</h1>
                        </div>
                        <div className={styles.collumData}>
                            <h2>Impact carbone</h2>
                            <div className={styles.rowData}>
                                <h1>{data.carbon_impact}</h1>
                                <h3>kCO2e</h3>
                            </div>
                        </div>
                    </div>
                </div>
                <div className={styles.rowButton}>
                    <button className={styles.button} onClick={() => navigate("/history")}>
                        Autre trajet
                    </button>
                    <button className={styles.button} onClick={() => alert("Le user veut enregistrer !")}>
                        Enregistrez
                    </button>
                </div>
            </div>
        </div>
    );
}

export default ResultPage;
