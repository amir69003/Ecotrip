import { useState } from "react";
import tripsData from "../assets/data/trip.json";
import usePagination from "../hooks/usePagination";
import HistoryTable from "../components/HistoryTable";
import Layout from "../components/Layout";
import { Trip } from "../assets/types/trip"; // 👈 Crée un vrai type Trip dans /types si besoin

function HistoryPage() {
    const [trips, setTrips] = useState<Trip[]>(tripsData); // ✅ Fix ici : types explicites

    const { currentData, currentPage, goToPage, totalPages, paginationRange } =
        usePagination(trips, 5);

    const handleDelete = (tripToDelete: Trip) => { // ✅ Fix ici aussi
        const updatedTrips = trips.filter((t) => t !== tripToDelete);
        setTrips(updatedTrips);
    };

    return (
        <Layout>
            <HistoryTable
                trips={currentData}
                currentPage={currentPage}
                goToPage={goToPage}
                totalPages={totalPages}
                paginationRange={paginationRange()}
                onDelete={handleDelete}
            />
        </Layout>
    );
}

export default HistoryPage;
