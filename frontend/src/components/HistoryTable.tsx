import styles from "../assets/styles/history.module.css";
import { useNavigate } from "react-router-dom";

type Trip = {
    departure: string;
    arrival: string;
    transport: string;
    carbon_impact: number;
};

type Props = {
    trips: Trip[];
    currentPage: number;
    goToPage: (page: number) => void;
    totalPages: number;
    paginationRange: number[];
    onDelete: (trip: Trip) => void; // nouvelle prop
};

export default function HistoryTable({
                                         trips,
                                         currentPage,
                                         goToPage,
                                         totalPages,
                                         paginationRange,
                                         onDelete
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
                    <th>Supprimer</th>
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
                        <td>
                            <button className={styles.deleteButton} onClick={() => onDelete(trip)}>
                                Supprimer
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
