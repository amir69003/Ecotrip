import {useNavigate} from "react-router";
import styles from "../assets/styles/history.module.css";
import {Trip} from "../model";

type Props = {
    trips: Trip[];
    currentPage: number;
    goToPage: (page: number) => void;
    totalPages: number;
    paginationRange: number[];
    onDelete: (trip: Trip) => void;
};

const HistoryTable = ({
                          trips,
                          currentPage,
                          goToPage,
                          totalPages,
                          paginationRange,
                          onDelete,
                      }: Props) => {
    const navigate = useNavigate();

    const handleNavigate = (trip: Trip) => {
        navigate(`/shared-trips/${trip.id}`);
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
                {trips.length > 0 ?
                    trips.map((trip, index) => (
                            <tr key={index} className={styles.row}>
                                <td onClick={() => handleNavigate(trip)}>
                                    <div className={styles.lieu}>
                                        <div>{trip.depart.split(',').slice(0, 4).join(',')}</div>
                                        {/*{trip.depart.region && (
                                            <div className={styles.sousTexte}>{trip.depart.region}</div>
                                        )}
                                        {trip.depart.pays && (
                                            <div className={styles.sousTexte}>{trip.depart.pays}</div>
                                        )}*/}
                                    </div>
                                </td>
                                <td onClick={() => handleNavigate(trip)}>
                                    <div className={styles.lieu}>
                                        <div>{trip.arrivee.split(',').slice(0, 4).join(',')}</div>
                                        {/*{trip.arrivee.region && (
                                            <div className={styles.sousTexte}>{trip.arrivee.region}</div>
                                        )}
                                        {trip.arrivee.pays && (
                                            <div className={styles.sousTexte}>{trip.arrivee.pays}</div>
                                        )}*/}
                                    </div>
                                </td>
                                <td onClick={() => handleNavigate(trip)}>
                                    {trip.moyenTransport}
                                </td>
                                <td onClick={() => handleNavigate(trip)}>
                                    <strong>{trip.kco2}</strong> kCO2e
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
                        )
                    ) : (
                        <tr>
                            <td colSpan={5} className={styles.noData}>
                                Aucun trajet trouvé
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>

            <div className={styles.pagination}>
                <button
                    onClick={() => goToPage(currentPage - 1)}
                    disabled={currentPage === 1 || totalPages === 0}
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
                    disabled={currentPage === totalPages || totalPages === 0}
                >
                    Page suivante
                </button>
            </div>
        </div>
    );
}

export default HistoryTable;