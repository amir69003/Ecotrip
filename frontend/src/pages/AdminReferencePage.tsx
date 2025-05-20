import {CustomLayout} from "../components";
import ReferenceForm from "../components/ReferenceForm";
import styles from "../assets/styles/referenceForm.module.css";
import {useAuthSession} from "../lib/auth";
import {useEffect, useState} from "react";
import {useMutation, useQuery} from "@tanstack/react-query";
import {CO2Reference} from "../model";
import {transportOptions} from "../lib/constant.ts";

export default function AdminReferencePage() {
    const {isAuthenticated, user} = useAuthSession();

    const [refs, setRefs] = useState<CO2Reference[]>([]);

    const {data, isError, error, isLoading} = useQuery({
        queryKey: ["co2s"],
        queryFn: async () => {
            const result: CO2Reference[] = [];
            for (let i = 1; i < transportOptions.length + 1; i++) {
                const response = await fetch(`/api/co2s/${i}`, {
                    method: "GET",
                    credentials: "include"
                });
                if (!response.ok) {
                    throw new Error("Erreur lors de la récupération des valeurs de référence");
                }
                const data = await response.json() as CO2Reference;
                result.push(data);
            }
            return result;
        },
        enabled: !!isAuthenticated
    });

    const {isError: isPatchError, error: patchError, isPending, mutate} = useMutation({
        mutationFn: async (ref: CO2Reference) => {
            const response = await fetch(`/api/co2s/${ref.id}`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify(ref)
            });
            if (!response.ok) {
                throw new Error("Erreur lors de l'enregistrement des valeurs de référence");
            }
        },
        onSuccess: () => {
            alert("Valeurs de référence mises à jour avec succès");
        },
    });

    useEffect(() => {
        if (data) {
            setRefs(data);
        }
    }, [data]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        const index = parseInt(name);
        setRefs((prev) => {
            if (prev) {
                //prev[index].kCo2 = parseFloat(value);
                const toUpdate = prev?.find(ref => ref.id === index)
                if (toUpdate) {
                    toUpdate.kCo2 = parseFloat(value);
                }
            }
            return prev;
        });
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        for (const r of refs) {
            mutate(r);
        }
    };

    if (user && !user.roles.includes("ROLE_ADMIN")) {
        return (
            <CustomLayout>
                <div style={{fontWeight: "bold"}}>Forbidden - Vous n'avez pas accès à cette page.</div>
            </CustomLayout>
        );
    }

    return (
        <CustomLayout>
            {!user ?
                (
                    <div style={{textAlign: "center"}}>Loading...</div>
                ) : (
                    <div className={styles.adminPage}>
                        {isLoading || isPending ? (
                            <div style={{textAlign: "center"}}>Loading...</div>
                        ) : (<>{
                            isError || isPatchError ? (
                                <div style={{textAlign: "center"}}>
                                    <p>{error?.message}</p>
                                    <p>{patchError?.message}</p>
                                    <p>Erreur lors de l'enregistrement des valeurs de référence</p>
                                </div>
                            ) : (
                                <ReferenceForm
                                    values={refs || []}
                                    onChange={handleChange}
                                    onSubmit={handleSubmit}
                                />
                            )
                        }</>)}
                    </div>
                )}
        </CustomLayout>
    );
} 