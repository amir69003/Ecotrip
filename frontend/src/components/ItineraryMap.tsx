import { MapContainer, TileLayer, Polyline } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import ChangeView from "./ChangeView";
import { useItineraryRoute } from "../hooks/useItineraryRoute";
import styles from "../assets/styles/result.module.css"; // 👈 ajout du module CSS

type ItineraryMapProps = {
    departure: string;
    arrival: string;
};

const ItineraryMap = ({ departure, arrival }: ItineraryMapProps) => {
    const { coords, error, loading } = useItineraryRoute(departure, arrival);

    return (
        <div className={styles.mapContainer}>
            <MapContainer
                center={[47, 2]}
                zoom={6}
                scrollWheelZoom={false}
                style={{ height: "100%", width: "100%", borderRadius: "10px" }}
            >
                <TileLayer
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution="&copy; OpenStreetMap contributors"
                />

                {loading && (
                    <div className={styles.loadingBox}>
                        Chargement de l'itinéraire...
                    </div>
                )}

                {!loading && coords.length > 0 && (
                    <>
                        <ChangeView coords={coords} />
                        <Polyline positions={coords} pathOptions={{ color: 'blue' }} />
                    </>
                )}

                {!loading && error && (
                    <div className={styles.errorBox}>
                        Erreur lors du chargement du trajet !
                    </div>
                )}
            </MapContainer>
        </div>
    );
};

export default ItineraryMap;
