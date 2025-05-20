import sharedTripsData from "../assets/data/trip_shared.json";

export async function fetchSharedTrips(useFakeData: boolean) {
    if (useFakeData) {
        // Simule un appel API avec la donnée locale
        return Promise.resolve(sharedTripsData);
    } else {
        // Remplace l'URL par celle de ton API réelle
        const response = await fetch("https://api.example.com/shared-trips");
        if (!response.ok) throw new Error("Erreur lors du chargement des trajets partagés");
        return response.json();
    }
} 