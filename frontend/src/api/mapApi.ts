import axios from "axios";

/**
 * Récupère les coordonnées [lat, lon] à partir d'une adresse.
 */
export const getCoordinates = async (location: string): Promise<[number, number]> => {
    const res = await axios.get(
        `https://nominatim.openstreetmap.org/search?format=json&q=${location}`
    );
    const data = res.data[0];
    return [parseFloat(data.lat), parseFloat(data.lon)];
};

/**
 * Récupère l'itinéraire sous forme de tableau de coordonnées [lat, lon]
 */
export const fetchRoute = async (
    departure: string,
    arrival: string
): Promise<[number, number][]> => {
    const [start, end] = await Promise.all([
        getCoordinates(departure),
        getCoordinates(arrival),
    ]);

    const response = await axios.get(
        `https://router.project-osrm.org/route/v1/driving/${start[1]},${start[0]};${end[1]},${end[0]}?overview=full&geometries=geojson`
    );

    return response.data.routes[0].geometry.coordinates.map(
        ([lng, lat]: [number, number]) => [lat, lng]
    );
};
