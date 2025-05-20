import {useNavigate} from "react-router";
import styles from "../assets/styles/home.module.css";
import {CustomLayout} from "../components";
import {ArrowBigDownDash} from "lucide-react";

function Home() {
    const navigate = useNavigate();

    return (
        <CustomLayout>
            <div className={styles.homePage}>
                <h1 style={{alignContent: "center", marginTop: "8rem"}}>
                    Avant de partir, jetez un oeil à votre empreinte
                </h1>
                <div style={{justifySelf: "center", marginTop: "1rem"}}>
                    <ArrowBigDownDash size={200}/>

                </div>
                <div style={{justifySelf: "center", marginTop: "3rem"}}>
                    <button onClick={() => navigate("/compute")} className={styles.button}
                            style={{backgroundColor: "white"}}>
                        Commencer à utiliser EcoTrip
                    </button>
                </div>
            </div>
        </CustomLayout>
    )
}

export default Home