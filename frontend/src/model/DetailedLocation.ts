
export type DetailedLocation = {
    display_name: string;
    lat: number;
    lon: number;
    address?: {
        city?: string;
        town?: string;
        village?: string;
        county?: string;
        municipality?: string;
        state?: string;
        region?: string;
        country?: string;
    };
    name?: string;
};

export default DetailedLocation;