import styles from "./../assets/styles/calcul.module.css";
import Header from "../components/Header.tsx";
import InputField from "../components/InputField.tsx";
import { CircleDot, MapPin } from "lucide-react";
import { useDestinationForm } from "../hooks/useDestinationForm.ts";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { Lieu } from "../assets/types/location";
import { Trip } from "../assets/types/trip.ts";

function CalculPage() {
    const navigate = useNavigate();
    const { itineraryData, handleChange, handleSubmit } = useDestinationForm(() => {
        if (!departureLieu || !arrivalLieu) {
            alert("Veuillez sélectionner votre départ et arrivée correctement.");
            return;
        }

        const carbonTrip: Trip = {
            departure: departureLieu,
            arrival: arrivalLieu,
            transport: selectedTransport, // ✅ utilise le transport choisi
            carbon_impact: 1.82 // (plus tard tu pourras changer selon le transport choisi)
        };

        navigate("/result", { state: carbonTrip });
    });

    const [departureLieu, setDepartureLieu] = useState<Lieu | null>(null);
    const [arrivalLieu, setArrivalLieu] = useState<Lieu | null>(null);
    const [selectedTransport, setSelectedTransport] = useState<string>("Transport en commun"); // ✅ Nouveau state

    return (
        <div className={styles.calculPage}>
            <Header isAuthenticated={false} />
            <form onSubmit={handleSubmit}>
                <div className={styles.containerComparer}>
                    <h2>Calculez votre trajet</h2>

                    <InputField
                        label="Départ"
                        id="departure"
                        name="departure"
                        value={itineraryData.departure}
                        onChange={handleChange}
                        icon={<CircleDot />}
                        onLieuSelect={setDepartureLieu}
                    />

                    <InputField
                        label="Arrivé"
                        id="arrival"
                        name="arrival"
                        value={itineraryData.arrival}
                        onChange={handleChange}
                        icon={<MapPin />}
                        onLieuSelect={setArrivalLieu}
                    />


                    <div className={styles.containerTransport}>
                        <h2>Moyen de transport : </h2>
                        <div className={styles.transportButtons}>
                            <button
                                type="button"
                                className={`${styles.transportButton} ${selectedTransport === "Transport en commun" ? styles.active : ""}`}
                                onClick={() => setSelectedTransport("Transport en commun")}
                            >
                                Transport en commun
                            </button>
                            <button
                                type="button"
                                className={`${styles.transportButton} ${selectedTransport === "Voiture" ? styles.active : ""}`}
                                onClick={() => setSelectedTransport("Voiture")}
                            >
                                Voiture
                            </button>
                            <button
                                type="button"
                                className={`${styles.transportButton} ${selectedTransport === "Avion" ? styles.active : ""}`}
                                onClick={() => setSelectedTransport("Avion")}
                            >
                                Avion
                            </button>
                            <button
                                type="button"
                                className={`${styles.transportButton} ${selectedTransport === "Bateau" ? styles.active : ""}`}
                                onClick={() => setSelectedTransport("Bateau")}
                            >
                                Bateau
                            </button>
                        </div>


                    </div>
                        <button className={styles.button} type="submit">
                            Comparez
                        </button>
                    </div>

            </form>
        </div>
    );
}

export default CalculPage;
