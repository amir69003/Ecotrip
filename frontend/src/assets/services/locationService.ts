// src/services/locationService.ts
import { Lieu, ItineraryItem } from "../types/location";

export const extractLieu = (item: ItineraryItem): Lieu => {
    if (!item || !item.address) return { displayName: item.display_name };

    const address = item.address;

    let ville =
        address.city ||
        address.town ||
        address.village ||
        address.county ||
        address.municipality;

    const region = address.state || address.region;
    const pays = address.country;

    // 💡 Correction pour éviter ville undefined
    if (!ville) {
        ville = region || pays || item.display_name;
    }

    return {
        ville,
        region,
        pays,
        displayName: item.display_name
    };
};

export const formatSuggestion = (item: ItineraryItem): string => {
    if (!item || !item.address) return item.display_name || "Inconnu";

    const { address } = item;

    const ville =
        address.city ||
        address.town ||
        address.village ||
        address.county ||
        address.municipality ||
        item.name;

    const region = address.state || address.region;
    const pays = address.country;

    return [ville, region, pays].filter(Boolean).join(", ") || item.display_name || "Inconnu";
};
