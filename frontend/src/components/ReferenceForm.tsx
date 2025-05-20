import React from "react";
import styles from "../assets/styles/referenceForm.module.css";
import {CO2Reference} from "../model";

type ReferenceFormProps = {
    values: CO2Reference[];
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    onSubmit: (e: React.FormEvent) => void;
};

export default function ReferenceForm({values, onChange, onSubmit}: ReferenceFormProps) {
    return (
        <form onSubmit={onSubmit} className={styles.formContainer}>
            <h1 className={styles.title}>Référence kCO2/km</h1>
            <div className={styles.fields}>
                {values.map((ref, index) => (
                    <div key={index} className={styles.row}>
                        <label className={styles.label}>{ref.transport}
                            <input type="number" min="0" step="any" name={ref.id.toString()} value={ref.kCo2}
                                   onChange={onChange} className={styles.input} required/>
                        </label>
                    </div>
                ))}
            </div>
            <div className={styles.buttonRow}>
                <button type="submit" className={styles.button}>Enregistrer</button>
            </div>
        </form>
    );
} 