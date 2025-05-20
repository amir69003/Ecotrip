import {DetailedLocation} from "../model/DetailedLocation.ts";

export async function fetchOpenStreetMapLocationSuggestions(query: string): Promise<DetailedLocation[]> {
    try {
        const response = await fetch(
            `https://nominatim.openstreetmap.org/search?format=json&addressdetails=1&q=${query}`
        );
        if (!response.ok) throw new Error("Erreur lors de la récupération des suggestions");
        const data = await response.json();
        return data as DetailedLocation[];
    } catch (error) {
        console.error("Erreur API OpenStreetMap :", error);
        return [];
    }
}

export const getCoordinates = async (location: string): Promise<[number, number]> => {
    const response = await fetch(
        `https://nominatim.openstreetmap.org/search?format=json&q=${location}`
    );
    const data = (await response.json())[0];
    return [parseFloat(data.lat), parseFloat(data.lon)];
};

export const fetchRoute = async (
    departure: string,
    arrival: string
): Promise<[number, number][]> => {
    const [start, end] = await Promise.all([
        getCoordinates(departure),
        getCoordinates(arrival),
    ]);

    const response = await fetch(
        `https://router.project-osrm.org/route/v1/driving/${start[1]},${start[0]};${end[1]},${end[0]}?overview=full&geometries=geojson`
    );

    if (!response.ok) {
        throw new Error("Erreur lors de la récupération de l'itinéraire");
    }

    const data = await response.json();

    return data.routes[0].geometry.coordinates.map(
        ([lng, lat]: [number, number]) => [lat, lng]
    );
};