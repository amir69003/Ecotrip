// src/components/ItineraryMap.tsx
import { MapContainer, TileLayer, Polyline } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import { useEffect, useState } from "react";
import ChangeView from "./ChangeView";
import { fetchRoute } from "../api/mapApi";

type ItineraryMapProps = {
    departure: string;
    arrival: string;
};

const ItineraryMap = ({ departure, arrival }: ItineraryMapProps) => {
    const [coords, setCoords] = useState<[number, number][]>([]);

    useEffect(() => {
        const loadRoute = async () => {
            try {
                const route = await fetchRoute(departure, arrival);
                setCoords(route);
            } catch (error) {
                console.error("Erreur lors du chargement de l'itinéraire :", error);
            }
        };

        loadRoute();
    }, [departure, arrival]);

    return (
        <MapContainer
            center={[47, 2]}
            zoom={6}
            scrollWheelZoom={false}
            style={{ height: "100%", width: "100%", borderRadius: "10px" }}
        >
            <TileLayer
                attribution='&copy; OpenStreetMap contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {coords.length > 0 && (
                <>
                    <ChangeView coords={coords} />
                    <Polyline positions={coords} color="blue" />
                </>
            )}
        </MapContainer>
    );
};

export default ItineraryMap;
