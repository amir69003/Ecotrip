import styles from "../assets/styles/sharedTrip.module.css";
import { useNavigate } from "react-router-dom";

type SharedTrip = {
    departure: string;
    arrival: string;
    transport: string;
    carbon_impact: number;
    email: string;
};

type Props = {
    trips: SharedTrip[];
    currentPage: number;
    goToPage: (page: number) => void;
    totalPages: number;
    paginationRange: number[];
    onContact: (trip: SharedTrip) => void;
};

export default function SharedTripsTable({
    trips,
    currentPage,
    goToPage,
    totalPages,
    paginationRange,
    onContact
}: Props) {
    const navigate = useNavigate();

    return (
        <div className={styles.container}>
            <table className={styles.table}>
                <thead>
                <tr>
                    <th>Départ</th>
                    <th>Arrivé</th>
                    <th>Transport utilisé</th>
                    <th>Impact carbone</th>
                    <th>Email</th>
                    <th>Contacter</th>
                </tr>
                </thead>
                <tbody>
                {trips.map((trip, index) => (
                    <tr key={index} className={styles.row}>
                        <td onClick={() => navigate("/result", { state: trip })}>{trip.departure}</td>
                        <td onClick={() => navigate("/result", { state: trip })}>{trip.arrival}</td>
                        <td onClick={() => navigate("/result", { state: trip })}>{trip.transport}</td>
                        <td onClick={() => navigate("/result", { state: trip })}>
                            <strong>{trip.carbon_impact}</strong> kCO2e
                        </td>
                        <td>{trip.email}</td>
                        <td>
                            <button className={styles.deleteButton} onClick={() => onContact(trip)}>
                                Contacter
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div className={styles.pagination}>
                <button onClick={() => goToPage(currentPage - 1)} disabled={currentPage === 1}>
                    Page précédente
                </button>
                {paginationRange.map((page) => (
                    <button
                        key={page}
                        onClick={() => goToPage(page)}
                        className={page === currentPage ? styles.active : ""}
                    >
                        {page}
                    </button>
                ))}
                <button onClick={() => goToPage(currentPage + 1)} disabled={currentPage === totalPages}>
                    Page suivante
                </button>
            </div>
        </div>
    );
} 