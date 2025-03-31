import {JSX} from "react";
import * as React from "react";
import styles from "../assets/styles/home.module.css";

type InputFieldProps = {
    label: string;
    type: string;
    name: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    icon?: JSX.Element;
};

const InputField: React.FC<InputFieldProps> = ({ label, type, name, value, onChange, icon }) => {
    return (
        <div>
            <h2>{label}</h2>
            <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
                {icon}
                <input className={styles.input} type={type} name={name} value={value} onChange={onChange} required />
            </div>
        </div>
    );
};

export default InputField;
