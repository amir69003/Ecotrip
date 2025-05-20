import {DetailedLocation, Location} from "../../model";

export const extractLieu = (item: DetailedLocation): Location => {
    if (!item?.address) {
        return {
            displayName: item.display_name,
            lat: item.lat,
            lon: item.lon
        };
    }

    const {address} = item;

    const ville =
        address.city ??
        address.town ??
        address.village ??
        address.county ??
        address.municipality ??
        address.state ??
        address.region ??
        address.country ??
        item.display_name;

    return {
        ville,
        region: address.state ?? address.region,
        pays: address.country,
        displayName: item.display_name,
        lat: item.lat,
        lon: item.lon
    };
};

export const formatSuggestion = (item: DetailedLocation): string => {
    if (!item?.address) return item.display_name || "Inconnu";

    const {address} = item;

    const ville =
        address.city ??
        address.town ??
        address.village ??
        address.county ??
        address.municipality ??
        item.name;

    const region = address.state ?? address.region;
    const pays = address.country;

    return [ville, region, pays].filter(Boolean).join(", ") || item.display_name || "Inconnu";
};
