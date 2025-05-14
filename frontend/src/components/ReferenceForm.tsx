import React from "react";
import styles from "../assets/styles/referenceForm.module.css";

export type ReferenceValues = {
    voiture: number | string;
    transport: number | string;
    bateau: number | string;
    velo: number | string;
    pied: number | string;
};

type ReferenceFormProps = {
    values: ReferenceValues;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    onSubmit: (e: React.FormEvent) => void;
    loading: boolean;
    onBack: () => void;
    error?: string | null;
    success?: string | null;
};

export default function ReferenceForm({ values, onChange, onSubmit, loading, onBack, error, success }: ReferenceFormProps) {
    return (
        <form onSubmit={onSubmit} className={styles.formContainer}>
            <h1 className={styles.title}>Référence kCO2/km</h1>
            <div className={styles.fields}>
                <div className={styles.row}>
                    <label className={styles.label}>Voiture
                        <input type="number" min="0" step="any" name="voiture" value={values.voiture} onChange={onChange} className={styles.input} required />
                    </label>
                    <label className={styles.label}>A pied
                        <input type="number" min="0" step="any" name="pied" value={values.pied} onChange={onChange} className={styles.input} required />
                    </label>
                </div>
                <div className={styles.row}>
                    <label className={styles.label}>Transport en commun
                        <input type="number" min="0" step="any" name="transport" value={values.transport} onChange={onChange} className={styles.input} required />
                    </label>
                    <label className={styles.label}>Velo
                        <input type="number" min="0" step="any" name="velo" value={values.velo} onChange={onChange} className={styles.input} required />
                    </label>
                </div>
                <div className={styles.row}>
                    <label className={styles.label}>Bateau
                        <input type="number" min="0" step="any" name="bateau" value={values.bateau} onChange={onChange} className={styles.input} required />
                    </label>
                </div>
            </div>
            {error && <div className={styles.error}>{error}</div>}
            {success && <div className={styles.success}>{success}</div>}
            <div className={styles.buttonRow}>
                <button type="button" onClick={onBack} className={styles.button}>Accueil</button>
                <button type="submit" disabled={loading} className={styles.button}>{loading ? 'Enregistrement...' : 'Enregistrez'}</button>
            </div>
        </form>
    );
} 