import styles from "../assets/styles/sharedTrip.module.css";
import {SharedTrip} from "../model";

type Props = {
    trips: SharedTrip[];
    currentPage: number;
    goToPage: (page: number) => void;
    totalPages: number;
    paginationRange: number[];
    onContact: (trip: SharedTrip) => void;
};

const SharedTripsTable = ({
                              trips,
                              currentPage,
                              goToPage,
                              totalPages,
                              paginationRange,
                              onContact
                          }: Props) => {
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
                {trips.length > 0 ?
                    trips.map((trip, index) => (
                            <tr key={index} className={styles.row}>
                                <td>{trip.trajet.depart.split(',').slice(0, 4).join(',')}</td>
                                <td>{trip.trajet.arrivee.split(',').slice(0, 4).join(',')}</td>
                                <td>{trip.trajet.moyenTransport}</td>
                                <td><strong>{trip.trajet.kco2}</strong> kCO2e</td>
                                <td>{trip.email}</td>
                                <td>
                                    <button className={styles.deleteButton} onClick={() => onContact(trip)}>
                                        Contacter
                                    </button>
                                </td>
                            </tr>
                        )
                    ) : (
                        <tr>
                            <td colSpan={5} className={styles.noData}>
                                Aucun trajet en commun trouvé
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>

            <div className={styles.pagination}>
                <button onClick={() => goToPage(currentPage - 1)} disabled={currentPage === 1 || totalPages === 0}>
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
                <button onClick={() => goToPage(currentPage + 1)}
                        disabled={currentPage === totalPages || totalPages === 0}>
                    Page suivante
                </button>
            </div>
        </div>
    );
}

export default SharedTripsTable;