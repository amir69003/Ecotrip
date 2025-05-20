import styles from "../assets/styles/result.module.css";
import Header from "../components/Header";
import ItineraryMap from "../components/ItineraryMap";
import {useLocation, useNavigate} from "react-router";
import {TripCarbonData} from "../model";
import {useAuthSession} from "../lib/auth";
import {useMutation} from "@tanstack/react-query";
import {transportOptions} from "../lib/constant";

function ResultPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const data = location.state as TripCarbonData;

    const {isAuthenticated} = useAuthSession();

    const {error, isPending, mutate} = useMutation({
        mutationFn: async (trip: TripCarbonData) => {
            const response = await fetch("http://127.0.0.1:8080/api/trajets", {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    depart: trip.departure.displayName,
                    departLatitude: trip.departure.lat,
                    departLongitude: trip.departure.lon,
                    arrivee: trip.arrival.displayName,
                    arriveeLatitude: trip.arrival.lat,
                    arriveeLongitude: trip.arrival.lon,
                    moyenTransport: transportOptions.find(option => option.id === trip.transport)?.name,
                    kco2: trip.kCo2
                }),
            });
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
        },
        onSuccess: () => {
            alert("Le trajet a été enregistré avec succès !");
        }
    });

    // fallback si aucun trajet n’est passé (accès direct à /result)
    if (!data) {
        return (
            <div className={styles.resultPage}>
                <Header/>
                <div className={styles.whiteContainer}>
                    <h2>Aucun trajet sélectionné.</h2>
                    <button className={styles.button} onClick={() => navigate("/compute")}>
                        Retour à la page de simulation
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className={styles.resultPage}>
            <Header/>
            <div className={styles.whiteContainer}>
                <div className={styles.mapResultContainer}>
                    <div className={styles.map}>
                        <ItineraryMap departure={data.departure.displayName} arrival={data.arrival.displayName}/>
                    </div>
                    <div className={styles.resultContainer}>
                        <div className={styles.collumData}>
                            <h2>Départ</h2>
                            <div>
                                <h1 style={{fontSize: 20}}>{data.departure.displayName}</h1>
                                <h2>{data.departure.ville}</h2>
                                <h2>{data.departure.region}</h2>
                                <h2>{data.departure.pays}</h2>
                            </div>

                        </div>
                        <div className={styles.collumData}>
                            <h2>Arrivé</h2>
                            <div>
                                <h1 style={{fontSize: 20}}>{data.arrival.displayName}</h1>
                                <h2>{data.arrival.ville}</h2>
                                <h2>{data.arrival.region}</h2>
                                <h2>{data.arrival.pays}</h2>
                            </div>

                        </div>
                        <div className={styles.collumData}>
                            <h2>Transport utilisé</h2>
                            <h1>{transportOptions.find(option => option.id === data.transport)?.name}</h1>
                        </div>
                        <div className={styles.collumData}>
                            <h2>Impact carbone</h2>
                            <div className={styles.rowData}>
                                <h1>{data.kCo2}</h1>
                                <h3>kCO2e</h3>
                            </div>
                        </div>
                    </div>
                </div>
                <div className={styles.rowButton}>
                    {isPending ?
                        (<p>Loading...</p>)
                        : (<>{isAuthenticated ? (
                            <>
                                {error && <p>Erreur : {error.message}</p>
                                }
                                <button className={styles.button} onClick={() => mutate(data)}>
                                    Enregistrez
                                </button>
                            </>
                        ) : (
                            <button className={styles.button} onClick={() => navigate("/login")}>
                                Connectez-vous pour enregistrer le trajet
                            </button>
                        )}
                        </>)
                    }
                </div>
            </div>
        </div>
    );
}

export default ResultPage;
