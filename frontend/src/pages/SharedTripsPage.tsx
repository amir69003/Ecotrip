import { useState, useEffect } from "react";
import { fetchSharedTrips } from "../api/sharedTripsApi";
import usePagination from "../hooks/usePagination";
import SharedTripsTable from "../components/SharedTripsTable";
import Layout from "../components/Layout.tsx";

const USE_FAKE_DATA = true; // Passe à false quand l'API sera prête

type SharedTrip = {
    departure: string;
    arrival: string;
    transport: string;
    carbon_impact: number;
    email: string;
};

function SharedTripsPage() {
    const [trips, setTrips] = useState<SharedTrip[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetchSharedTrips(USE_FAKE_DATA)
            .then((data) => {
                setTrips(data);
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    const { currentData, currentPage, goToPage, totalPages, paginationRange } =
        usePagination(trips, 7);

    // Pas de suppression ici, juste contact
    const handleContact = (trip: SharedTrip) => {
        window.location.href = `mailto:${trip.email}?subject=Contact%20EcoTrip&body=Bonjour,%20je%20suis%20intéressé%20par%20votre%20itinéraire%20de%20${trip.departure}%20à%20${trip.arrival}.`;
    };

    if (loading) return <Layout><div>Chargement...</div></Layout>;
    if (error) return <Layout><div>Erreur : {error}</div></Layout>;

    return (
        <Layout>
            <SharedTripsTable
                trips={currentData}
                currentPage={currentPage}
                goToPage={goToPage}
                totalPages={totalPages}
                paginationRange={paginationRange()}
                onContact={handleContact}
            />
        </Layout>
    );
}

export default SharedTripsPage; 