import usePagination from "../hooks/usePagination";
import HistoryTable from "../components/HistoryTable";
import CustomLayout from "../components/CustomLayout";
import {Trip} from "../model";
import {useMutation, useQuery} from "@tanstack/react-query";
import {useAuthSession} from "../lib/auth.ts";

function HistoryPage() {
    const {isAuthenticated} = useAuthSession();

    const {data, isError, error, isLoading, refetch} = useQuery({
        queryKey: ["trips"],
        queryFn: async () => {
            const response = await fetch("http://127.0.0.1:8080/api/users/trajets", {
                method: "GET",
                credentials: "include"
            });
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            const jsonResponse = await response.json();
            return jsonResponse.trajets as Trip[];
        },
        enabled: !!isAuthenticated
    });

    const {mutate, error: deteleError, isPending: isDeletePending} = useMutation({
        mutationFn: async (tripToDelete: Trip) => {
            const response = await fetch(`http://127.0.0.1:8080/api/trajets/${tripToDelete.id}`, {
                method: "DELETE",
                credentials: "include"
            });
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
        },
        onSuccess: () => {
            refetch();
        }
    });

    const {currentData, currentPage, goToPage, totalPages, paginationRange} = usePagination(data ?? [], 5);

    const handleDelete = (tripToDelete: Trip) => {
        mutate(tripToDelete);
    };

    return (
        <CustomLayout>
            {isLoading || isDeletePending ? (
                <div className="flex justify-center items-center h-screen">
                    <p className="text-2xl">Loading...</p>
                </div>
            ) : (
                <>
                    {isError || deteleError || error ? (
                        <div className="flex justify-center items-center h-screen">
                            <p className="text-2xl text-red-500">Erreur : {error?.message || deteleError?.message}</p>
                            <p className="text-2xl text-red-500">Raffraichissez la page</p>
                        </div>
                    ) : (
                        <HistoryTable
                            trips={currentData}
                            currentPage={currentPage}
                            goToPage={goToPage}
                            totalPages={totalPages}
                            paginationRange={paginationRange()}
                            onDelete={handleDelete}
                        />
                    )}
                </>
            )}
        </CustomLayout>
    );
}

export default HistoryPage;
