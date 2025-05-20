import React, {JSX} from "react";
import styles from "../assets/styles/input.module.css";
import {useSuggestions} from "../hooks/useSuggestions";
import {DetailedLocation, Location} from "../model";

type InputFieldProps = {
    label: string;
    id: string;
    name: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    icon?: JSX.Element;
    onLieuSelect: (lieu: Location) => void;
};

const InputField: React.FC<InputFieldProps> = ({label, id, name, value, onChange, icon, onLieuSelect}) => {
    const {
        suggestions,
        selectSuggestion,
        clearSuggestions,
        resetSelection,
    } = useSuggestions(value);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        onChange(e);
        resetSelection();
    };

    const formatLieu = (lieu: Location) => {
        return [lieu.ville, lieu.region, lieu.pays].filter(Boolean).join(", ");
    };

    const handleLieuSelect = (item: DetailedLocation) => {
        const lieu = selectSuggestion(item);
        const formatted = formatLieu(lieu);
        onChange({target: {name, value: formatted}} as React.ChangeEvent<HTMLInputElement>);
        clearSuggestions();
        onLieuSelect(lieu);
    };

    return (
        <div className={styles.inputField}>
            <h2>{label}</h2>
            <div className={styles.inputContainer} style={{position: "relative"}}>
                {icon}
                <input
                    className={styles.input}
                    type="text"
                    id={id}
                    name={name}
                    value={value}
                    onChange={handleInputChange}
                    onBlur={clearSuggestions}
                    required
                    autoComplete="off"
                />

                {suggestions.length > 0 && (
                    <div className={styles.suggestionBox}>
                        {suggestions.map((item, i) => (
                            <div
                                key={i + item.name}
                                className={styles.suggestionItem}
                                onClick={() => handleLieuSelect(item)}
                            >
                                {item.display_name}
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default InputField;
