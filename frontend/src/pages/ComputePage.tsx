import styles from "./../assets/styles/calcul.module.css";
import Header from "../components/Header";
import InputField from "../components/InputField";
import {CircleDot, MapPin} from "lucide-react";
import {useDestinationForm} from "../hooks/useDestinationForm";
import {useNavigate} from "react-router";

type CarbonData = {
    departure: string;
    arrival: string;
    transport: string;
    carbon_impact: number;
};

function ComputePage() {
    const navigate = useNavigate();

    const {itineraryData, handleChange, handleSubmit} = useDestinationForm(() => {
        // Simule un calcul d'impact carbone
        const carbonTrip: CarbonData = {
            departure: itineraryData.departure,
            arrival: itineraryData.arrival,
            transport: "Train", // Tu peux adapter en fonction du formulaire
            carbon_impact: 1.82 // Idéalement calculé via API
        };

        // Rediriger avec state (passage de données)
        navigate("/result", {state: carbonTrip});
    });


    return (
        <div className={styles.calculPage}>
            <Header/>
            <form onSubmit={handleSubmit}>
                <div className={styles.containerComparer}>

                    <h2>Calculez votre trajet</h2>
                    <InputField type="departure" label="Départ" id="departure" name="departure"
                                value={itineraryData.departure} onChange={handleChange} icon={<CircleDot/>}/>
                    <InputField type="arrival" label="Arrivé" id="arrival" name="arrival"
                                value={itineraryData.arrival} onChange={handleChange} icon={<MapPin/>}/>
                    <button className={styles.button}>
                        Comparez
                    </button>

                </div>
            </form>
        </div>
    )
}

export default ComputePage;