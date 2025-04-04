import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import CalculPage from './pages/CalculPage.tsx'
import Home from "./pages/Home.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Home />
      <CalculPage/>
  </StrictMode>,
)
