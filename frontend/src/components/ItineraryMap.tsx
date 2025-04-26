import {MapContainer, Polyline, TileLayer} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import ChangeView from "./ChangeView";
import {useItineraryRoute} from "../hooks/useItineraryRoute";

type ItineraryMapProps = {
    departure: string;
    arrival: string;
};

const ItineraryMap = ({departure, arrival}: ItineraryMapProps) => {
    const {coords, error, loading} = useItineraryRoute(departure, arrival);

    if (error) {
        console.warn(error)
    }

    return (
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
            {!loading && coords.length > 0 && (
                <>
                    <ChangeView coords={coords}/>
                    <Polyline positions={coords}
                        // color="blue"
                    />
                </>
            )}
        </MapContainer>
    );
};

export default ItineraryMap;
