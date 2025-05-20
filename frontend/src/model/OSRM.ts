export interface OSRMRoute {
    legs: OSRMLeg[];
    weight_name: string;
    geometry: {
        coordinates: [number, number][];
        type: "LineString";
    };
    weight: number;
    duration: number;
    distance: number;
}

export interface OSRMLeg {
    steps: unknown[];
    weight: number;
    summary: string;
    duration: number;
    distance: number;
}

export interface OSRMWaypoint {
    hint: string;
    location: [number, number];
    name: string;
    distance: number;
}
