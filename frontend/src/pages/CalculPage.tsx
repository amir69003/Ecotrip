import styles from "./../assets/styles/calcul.module.css";
import Header from "../components/Header.tsx";
import InputField from "../components/InputField.tsx";
import * as React from "react";
import {useState} from "react";
import { CircleDot, MapPin } from "lucide-react";


type ItineraryData = {
    departure: string,
    arrival: string,
}

function CalculPage(){

    const [itineraryData, setItineraryData] = useState<ItineraryData>({
        departure:'',
        arrival:''
    });
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name , value } = e.target;
        setItineraryData(prevData => ({...prevData, [name]: value}));
    }
return (
    <>
        <div className={styles.calculPage}>
            <Header isAuthenticated={false} />
            <div className={styles.containerComparer}>
                <h2>Calculez votre trajet</h2>
                <InputField type="departure" label="Départ" id="departure" name="departure" value={itineraryData.departure} onChange={handleChange} icon={<CircleDot/>} />
                <InputField type="arrival" label="Arrivé" id="arrival" name="arrival" value={itineraryData.arrival} onChange={handleChange} icon={<MapPin/>} />
                <button className={styles.button} onClick={() => alert("Le user veut se connecter !")}>
                    Comparez
                </button>
            </div>
        </div>
    </>
)
}
export default CalculPage;