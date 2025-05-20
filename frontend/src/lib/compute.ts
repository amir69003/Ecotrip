import {DetailedLocation, Location} from "../model";

const EARTH_RADIUS_KM = 6371;

export const computeDistance = (departure: Location | DetailedLocation, arrival: Location | DetailedLocation): number => {
    const toRadians = (degrees: number) => degrees * (Math.PI / 180);

    const departureLatRad = toRadians(departure.lat);
    const departureLonRad = toRadians(departure.lon);
    const arrivalLatRad = toRadians(arrival.lat);
    const arrivalLonRad = toRadians(arrival.lon);

    const dLat = arrivalLatRad - departureLatRad;
    const dLon = arrivalLonRad - departureLonRad;

    const a =
        Math.sin(dLat / 2) ** 2 +
        Math.cos(departureLatRad) * Math.cos(arrivalLatRad) *
        Math.sin(dLon / 2) ** 2;

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS_KM * c;
};