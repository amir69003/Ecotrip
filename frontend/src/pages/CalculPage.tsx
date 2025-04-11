import styles from "./../assets/styles/calcul.module.css";
import Header from "../components/Header.tsx";
import InputField from "../components/InputField.tsx";
import { CircleDot, MapPin } from "lucide-react";
import {useDestinationForm} from "../hooks/useDestinationForm.ts";

function CalculPage(){
    const { itineraryData, handleChange, handleSubmit } = useDestinationForm((data) => {
        alert("Le départ : " +  itineraryData.departure + " et le mot de passe " + itineraryData.arrival);
        // 🔜 Ici, tu feras ton appel API réel
    });


    return (
    <>
        <div className={styles.calculPage}>
            <Header isAuthenticated={false} />
            <form onSubmit={handleSubmit}>
                <div className={styles.containerComparer}>

                        <h2>Calculez votre trajet</h2>
                        <InputField type="departure" label="Départ" id="departure" name="departure" value={itineraryData.departure} onChange={handleChange} icon={<CircleDot/>} />
                        <InputField type="arrival" label="Arrivé" id="arrival" name="arrival" value={itineraryData.arrival} onChange={handleChange} icon={<MapPin/>} />
                        <button className={styles.button}>
                            Comparez
                        </button>

                </div>
            </form>
        </div>
    </>
)
}
export default CalculPage;