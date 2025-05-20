import React from "react";
import {Header} from "./";
import styles from "../assets/styles/layout.module.css";

type CustomLayoutProps = {
    children: React.ReactNode;
};

const CustomLayout: React.FC<CustomLayoutProps> = ({children}) => {
    return (
        <div className={styles.layout}>
            <Header/>
            {children}
        </div>
    );
}

export default CustomLayout;