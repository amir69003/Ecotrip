import {JSX} from "react";
import * as React from "react";
import styles from "../assets/styles/input.module.css";

type InputFieldProps = {
    label: string;
    type: string;
    id: string;
    name: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    icon?: JSX.Element;
};

const InputField: React.FC<InputFieldProps> = ({ label, type, id, name, value, onChange, icon }) => {
    return (
        <div className={styles.inputField}>
            <h2>{label}</h2>
            <div className={styles.inputContainer}>
                {icon}
                <input className={styles.input} type={type} id={id} name={name} value={value} onChange={onChange} required />
            </div>
        </div>
    );
};

export default InputField;
