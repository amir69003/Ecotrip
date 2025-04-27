import { useNavigate } from "react-router-dom";
import styles from "../assets/styles/history.module.css";
import { Trip } from "../assets/types/trip.ts";

type Props = {
    trips: Trip[];
    currentPage: number;
    goToPage: (page: number) => void;
    totalPages: number;
    paginationRange: number[];
    onDelete: (trip: Trip) => void;
};

export default function HistoryTable({
                                         trips,
                                         currentPage,
                                         goToPage,
                                         totalPages,
                                         paginationRange,
                                         onDelete,
                                     }: Props) {
    const navigate = useNavigate();

    const handleNavigate = (trip: Trip) => {
        navigate("/result", { state: trip });
    };

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
                        <td onClick={() => handleNavigate(trip)}>
                            <div className={styles.lieu}>
                                <div>{trip.departure.ville || trip.departure.displayName}</div>
                                {trip.departure.region && (
                                    <div className={styles.sousTexte}>{trip.departure.region}</div>
                                )}
                                {trip.departure.pays && (
                                    <div className={styles.sousTexte}>{trip.departure.pays}</div>
                                )}
                            </div>
                        </td>
                        <td onClick={() => handleNavigate(trip)}>
                            <div className={styles.lieu}>
                                <div>{trip.arrival.ville || trip.arrival.displayName}</div>
                                {trip.arrival.region && (
                                    <div className={styles.sousTexte}>{trip.arrival.region}</div>
                                )}
                                {trip.arrival.pays && (
                                    <div className={styles.sousTexte}>{trip.arrival.pays}</div>
                                )}
                            </div>
                        </td>
                        <td onClick={() => handleNavigate(trip)}>
                            {trip.transport}
                        </td>
                        <td onClick={() => handleNavigate(trip)}>
                            <strong>{trip.carbon_impact}</strong> kCO2e
                        </td>
                        <td>
                            <button
                                className={styles.deleteButton}
                                onClick={() => onDelete(trip)}
                            >
                                Supprimer
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div className={styles.pagination}>
                <button
                    onClick={() => goToPage(currentPage - 1)}
                    disabled={currentPage === 1}
                >
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

                <button
                    onClick={() => goToPage(currentPage + 1)}
                    disabled={currentPage === totalPages}
                >
                    Page suivante
                </button>
            </div>
        </div>
    );
}
