import {BrowserRouter, Route, Routes} from 'react-router';

import Home from "./pages/Home";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";

import ComputePage from "./pages/ComputePage";
import ResultPage from "./pages/ResultPage";
import HistoryPage from "./pages/HistoryPage";
import ProtectedPage from "./pages/ProtectedPage";
import SharedTripsPage from "./pages/SharedTripsPage";
import AdminReferencePage from "./pages/AdminReferencePage";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/compute" element={<ComputePage/>}/>
                <Route path="/result" element={<ResultPage/>}/>
                <Route path="/history" element={<ProtectedPage><HistoryPage/></ProtectedPage>}/>
                <Route path="/shared-trips/:id" element={<ProtectedPage><SharedTripsPage/></ProtectedPage>}/>
                <Route path="/admin/reference" element={<ProtectedPage><AdminReferencePage/></ProtectedPage>}/>
            </Routes>
        </BrowserRouter>
    )
}

export default App
