import styles from "../assets/styles/result.module.css";
import Header from "../components/Header.tsx";
import ItineraryMap from "../components/ItineraryMap.tsx";
type CarbonData = {
    departure: string,
    arrival: string,
    transport: string,
    carbon_impact: number
}
const fakeData: CarbonData = {
    departure: "Lyon",
    arrival: "Bari",
    transport: "Train",
    carbon_impact: 4.00
}
function ResultPage(){
    return (
        <>
            <div className={styles.resultPage}>
                <Header isAuthenticated={true}/>
                <div className={styles.whiteContainer}>
                    <div className={styles.mapResultContainer}>
                        <div className={styles.map}>
                            <ItineraryMap departure={fakeData.departure} arrival={fakeData.arrival} />
                        </div>
                        <div className={styles.resultContainer}>
                            <div className={styles.collumData}>
                                <h2>Départ</h2>
                                <h1>{fakeData.departure}</h1>
                            </div>
                            <div className={styles.collumData}>
                                <h2>Arrivé</h2>
                                <h1>{fakeData.arrival}</h1>
                            </div>
                            <div className={styles.collumData}>
                                <h2>Transport utilisé</h2>
                                <h1>{fakeData.transport}</h1>
                            </div>
                            <div className={styles.collumData}>
                                <h2>Impact carbone</h2>
                                <div className={styles.rowData}>
                                    <h1>{fakeData.carbon_impact}</h1>
                                    <h3>kCO2e</h3>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className={styles.rowButton}>
                        <button className={styles.button} onClick={() => alert("Le user veut se connecter !")}>
                            Autre trajet
                        </button>
                        <button className={styles.button} onClick={() => alert("Le user veut se connecter !")}>
                            Enregistrez
                        </button>
                    </div>
                </div>

            </div>
        </>
    )
}
export default ResultPage;