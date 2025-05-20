import usePagination from "../hooks/usePagination";
import SharedTripsTable from "../components/SharedTripsTable";
import {CustomLayout} from "../components";
import {useQuery} from "@tanstack/react-query";
import {SharedTrip} from "../model";
import {useAuthSession} from "../lib/auth";
import {useParams} from "react-router";

function SharedTripsPage() {
    const {isAuthenticated} = useAuthSession();
    const {id} = useParams();

    const {data, isError, error, isLoading} = useQuery({
        queryKey: ["sharedTrips"],
        queryFn: async () => {
            const response = await fetch(`http://127.0.0.1:8080/api/trajets/${id}/communs`,
                {
                    method: "GET",
                    credentials: "include",
                });
            if (!response.ok) {
                throw new Error("Erreur lors du chargement des trajets en commun");
            }
            return await response.json() as SharedTrip[];
        },
        enabled: !!isAuthenticated,
    });

    console.log(data);

    const {currentData, currentPage, goToPage, totalPages, paginationRange} = usePagination(data ?? [], 7);

    const handleContact = (trip: SharedTrip) => {
        window.location.href = `mailto:${trip.email}?subject=Contact%20EcoTrip&body=Bonjour,%20je%20suis%20intéressé%20par%20votre%20itinéraire%20de%20${trip.trajet.depart}%20à%20${trip.trajet.arrivee}.`;
    };

    return (
        <CustomLayout>
            {isLoading ?
                (<div>Loading...</div>)
                : (<>
                    {isError ?
                        (<>
                            <p>{error?.message}</p>
                            <p>Rafraichissez la page</p>
                        </>)
                        :
                        (<SharedTripsTable
                            trips={currentData}
                            currentPage={currentPage}
                            goToPage={goToPage}
                            totalPages={totalPages}
                            paginationRange={paginationRange()}
                            onContact={handleContact}
                        />)}
                </>)
            }
        </CustomLayout>
    );
}

export default SharedTripsPage; 