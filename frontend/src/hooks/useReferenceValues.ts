import { useState, useEffect } from "react";
import { fetchReferenceValues, updateReferenceValues } from "../api/referenceApi";
import { ReferenceValues } from "../components/ReferenceForm";

const USE_FAKE_DATA = true; // Passe à false quand l'API sera prête

export function useReferenceValues() {
    const [values, setValues] = useState<ReferenceValues>({
        voiture: "",
        transport: "",
        bateau: "",
        velo: "",
        pied: ""
    });
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    useEffect(() => {
        setLoading(true);
        fetchReferenceValues(USE_FAKE_DATA)
            .then((data) => {
                setValues({
                    voiture: data.voiture,
                    transport: data.transport,
                    bateau: data.bateau,
                    velo: data.velo,
                    pied: data.pied
                });
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setValues((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setSaving(true);
        setError(null);
        setSuccess(null);
        try {
            await updateReferenceValues({
                voiture: Number(values.voiture),
                transport: Number(values.transport),
                bateau: Number(values.bateau),
                velo: Number(values.velo),
                pied: Number(values.pied)
            }, USE_FAKE_DATA);
            setSuccess("Valeurs enregistrées avec succès !");
        } catch (err: any) {
            setError(err.message || "Erreur lors de l'enregistrement");
        } finally {
            setSaving(false);
        }
    };

    return {
        values,
        loading,
        saving,
        error,
        success,
        setSuccess,
        handleChange,
        handleSubmit
    };
} 