import styles from "./../assets/styles/calcul.module.css";
import Header from "../components/Header";
import InputField from "../components/InputField";
import {CircleDot, MapPin} from "lucide-react";
import {useNavigate} from "react-router";
import {useState} from "react";
import {useMutation} from "@tanstack/react-query";
import {Location, OSRMRoute, OSRMWaypoint, TripCarbonData} from "../model";
import {computeDistance} from "../lib/compute";

const transportOptions = [
    {id: 1, name: "Vélo électrique"},
    {id: 2, name: "Trottinette électrique"},
    {id: 3, name: "Train"},
    {id: 4, name: "Voiture électrique"},
    {id: 5, name: "Bus"},
    {id: 6, name: "Moto thermique"},
    {id: 7, name: "Voiture thermique"},
    {id: 8, name: "Avion"},
    {id: 9, name: "Bateau (type croisière)"},
];

function ComputePage() {
    const navigate = useNavigate();

    const [itineraryData, setItineraryData] = useState({
        departure: '',
        arrival: ''
    });

    const [departureLieu, setDepartureLieu] = useState<Location | null>(null);
    const [arrivalLieu, setArrivalLieu] = useState<Location | null>(null);
    const [selectedTransport, setSelectedTransport] = useState<number>(0);

    const {isError, error, isPending, mutate} = useMutation({
        mutationFn: async ({departure, arrival}: { departure: Location, arrival: Location }) => {
            let distanceKm = 0;
            if (selectedTransport == 8 || selectedTransport == 9) {
                distanceKm = Math.round(computeDistance(departure, arrival));
            } else {
                let apiRoute = "driving";
                if (selectedTransport == 1 || selectedTransport == 2) {
                    apiRoute = "cycling";
                }
                const routingResponse = await fetch(`https://router.project-osrm.org/route/v1/${apiRoute}/${departure.lon},${departure.lat};${arrival.lon},${arrival.lat}?overview=full&geometries=geojson`);
                if (!routingResponse.ok) {
                    throw new Error('Network response was not ok');
                }
                const data: {
                    routes: OSRMRoute[];
                    waypoints: OSRMWaypoint [];
                } = await routingResponse.json();
                if (!data.routes || data.routes.length === 0) {
                    throw new Error('No routes found');
                }
                distanceKm = Math.round(data.routes[0].distance / 1000);
            }
            const response = await fetch(`http://127.0.0.1:8080/api/trajets/${selectedTransport}/${distanceKm}`, {credentials: 'include'});
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const responseData = await response.json();
            return {
                departure,
                arrival,
                transport: selectedTransport,
                kCo2: responseData?.kco2
            } as TripCarbonData;
        },
        onSuccess: (data) => {
            navigate("/result", {state: data});
        }
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setItineraryData(prevData => ({...prevData, [name]: value}));
    }

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!departureLieu || !arrivalLieu) {
            alert("Veuillez sélectionner votre départ et arrivée correctement.");
            return;
        }
        mutate({departure: departureLieu, arrival: arrivalLieu});
    }

    return (
        <div className={styles.calculPage}>
            <Header/>
            <form onSubmit={handleSubmit}>
                <div className={styles.containerComparer}>
                    {isPending ? (
                        <p>Loading...</p>
                    ) : (<>
                            <h2>Calculez votre trajet</h2>

                            <InputField
                                label="Départ"
                                id="departure"
                                name="departure"
                                value={itineraryData.departure}
                                onChange={handleChange}
                                icon={<CircleDot/>}
                                onLieuSelect={setDepartureLieu}
                            />

                            <InputField
                                label="Arrivé"
                                id="arrival"
                                name="arrival"
                                value={itineraryData.arrival}
                                onChange={handleChange}
                                icon={<MapPin/>}
                                onLieuSelect={setArrivalLieu}
                            />


                            <div className={styles.containerTransport}>
                                <h2>Moyen de transport : </h2>
                                <div className={styles.transportButtons}>
                                    {
                                        transportOptions.map((option) => (
                                            <button
                                                key={option.id}
                                                type="button"
                                                className={`${styles.transportButton} ${selectedTransport === option.id ? styles.active : ""}`}
                                                onClick={() => setSelectedTransport(option.id)}
                                            >
                                                {option.name}
                                            </button>
                                        ))
                                    }
                                </div>


                            </div>
                            {isError && error && (<p>Erreur : {error.message}</p>)}
                            <button className={styles.button} type="submit">
                                Comparez
                            </button>
                        </>
                    )}
                </div>

            </form>
        </div>
    );
}

export default ComputePage;