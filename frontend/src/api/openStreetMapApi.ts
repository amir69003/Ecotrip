// src/api/openStreetMapApi.ts
import axios from "axios";
import { ItineraryItem } from "../assets/types/location";

export async function fetchOpenStreetMapSuggestions(query: string): Promise<ItineraryItem[]> {
    try {
        const response = await axios.get<ItineraryItem[]>(
            `https://nominatim.openstreetmap.org/search?format=json&addressdetails=1&q=${query}`
        );
        return response.data.slice(0, 5); // Toujours slice pour limiter
    } catch (error) {
        console.error("Erreur API OpenStreetMap :", error);
        return [];
    }
}
