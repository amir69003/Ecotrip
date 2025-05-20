import { useEffect, useState } from "react";
import { fetchRoute } from "../lib/openStreetMap.ts";

export function useItineraryRoute(departure: string, arrival: string) {
    const [coords, setCoords] = useState<[number, number][]>([]);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const loadRoute = async () => {
            setLoading(true);
            setError(null);
            try {
                const route = await fetchRoute(departure, arrival);
                setCoords(route);
            } catch (err) {
                console.error("Erreur de chargement de l'itinéraire :", err);
                setError("Impossible de charger l'itinéraire");
                setCoords([]);
            } finally {
                setLoading(false);
            }
        };

        loadRoute();
    }, [departure, arrival]);

    return { coords, error, loading };
}
