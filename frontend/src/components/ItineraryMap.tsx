import {MapContainer, Polyline, TileLayer} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import ChangeView from "./ChangeView";
import {useItineraryRoute} from "../hooks/useItineraryRoute";
import styles from "../assets/styles/result.module.css";

type ItineraryMapProps = {
    departure: string;
    arrival: string;
};

const ItineraryMap = ({departure, arrival}: ItineraryMapProps) => {
    const {coords, error, loading} = useItineraryRoute(departure, arrival);

    return (
        <div className={styles.mapContainer}>
            <MapContainer
                // center={[47, 2]}
                // zoom={6}
                // scrollWheelZoom={false}
                style={{height: "100%", width: "100%", borderRadius: "10px"}}
            >
                <TileLayer
                    // attribution='&copy; OpenStreetMap contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />

                {loading && (
                    <div className={styles.loadingBox}>
                        Chargement de l'itinéraire...
                    </div>
                )}

                {!loading && coords.length > 0 && (
                    <>
                        <ChangeView coords={coords}/>
                        <Polyline positions={coords} pathOptions={{ color: 'blue' }}
                            // color="blue"
                        />
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
