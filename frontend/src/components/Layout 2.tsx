import React from "react";
import Header from "./Header";
import styles from "../assets/styles/layout.module.css";

type Props = {
    children: React.ReactNode;
};

export default function Layout({ children }: Props) {
    return (
        <div className={styles.layout}>
            <Header isAuthenticated={true} />
            {children}
        </div>
    );
}
