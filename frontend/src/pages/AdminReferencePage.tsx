import Layout from "../components/Layout";
import ReferenceForm from "../components/ReferenceForm";
import { useReferenceValues } from "../hooks/useReferenceValues";
import { useNavigate } from "react-router-dom";
import styles from "../assets/styles/referenceForm.module.css";

export default function AdminReferencePage() {
    const {
        values,
        loading,
        saving,
        error,
        success,
        handleChange,
        handleSubmit
    } = useReferenceValues();
    const navigate = useNavigate();

    const handleBack = () => {
        navigate("/calcul");
    };

    return (
        <Layout>
            <div className={styles.adminPage}>
                <ReferenceForm
                    values={values}
                    onChange={handleChange}
                    onSubmit={handleSubmit}
                    loading={saving || loading}
                    onBack={handleBack}
                    error={error}
                    success={success}
                />
            </div>
        </Layout>
    );
} 