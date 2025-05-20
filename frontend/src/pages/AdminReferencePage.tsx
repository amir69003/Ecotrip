import {CustomLayout} from "../components";
import ReferenceForm from "../components/ReferenceForm";
import { useReferenceValues } from "../hooks/useReferenceValues";
import { useNavigate } from "react-router";
import styles from "../assets/styles/referenceForm.module.css";

export default function AdminReferencePage() {
    const navigate = useNavigate();
    const {
        values,
        loading,
        saving,
        error,
        success,
        handleChange,
        handleSubmit
    } = useReferenceValues();

    const handleBack = () => {
        navigate("/calcul");
    };

    return (
        <CustomLayout>
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
        </CustomLayout>
    );
} 