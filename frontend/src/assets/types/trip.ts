import { Lieu } from "./location";

export type Trip = {
    departure: Lieu;
    arrival: Lieu;
    transport: string;
    carbon_impact: number;
};
