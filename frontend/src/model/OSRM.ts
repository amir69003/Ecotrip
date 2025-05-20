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
    steps: unknown[]; // or define OSRMStep[] if using `steps=true`
    weight: number;
    summary: string;
    duration: number;
    distance: number;
}

export interface OSRMWaypoint {
    hint: string;
    location: [number, number]; // [longitude, latitude]
    name: string;
    distance: number;
}
