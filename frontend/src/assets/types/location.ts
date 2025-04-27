export type Lieu = {
    ville?: string;
    region?: string;
    pays?: string;
    displayName: string;
};
export type ItineraryItem = {
    display_name: string; // Le nom affiché (ex : "Lyon, Rhône, France")
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