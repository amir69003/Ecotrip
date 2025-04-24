import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import Home from './pages/Home.tsx';
import CalculPage from './pages/CalculPage.tsx';
import ResultPage from './pages/ResultPage.tsx';
import HistoryPage from './pages/HistoryPage.tsx';
import RegisterPage from "./pages/RegisterPage.tsx";

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/calcul" element={<CalculPage />} />
                <Route path="/result" element={<ResultPage />} />
                <Route path="/history" element={<HistoryPage />} />
            </Routes>
        </BrowserRouter>
    </StrictMode>
);
