import { useState } from "react";
import Header from "../components/Header";
import tripsData from "../assets/data/trip.json";
import usePagination from "../hooks/usePagination";
import HistoryTable from "../components/HistoryTable";
import Layout from "../components/Layout.tsx";

function HistoryPage() {
    const [trips, setTrips] = useState(tripsData);

    const { currentData, currentPage, goToPage, totalPages, paginationRange } =
        usePagination(trips, 7);

    const handleDelete = (tripToDelete: typeof trips[0]) => {
        const updatedTrips = trips.filter((t) => t !== tripToDelete);
        setTrips(updatedTrips);

        // 🔜 Plus tard : tu pourras ici appeler ton API :
        // fetch('/api/trips/delete', { method: 'POST', body: JSON.stringify(tripToDelete) })
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
