import {StrictMode} from 'react';
import {createRoot} from 'react-dom/client';
import './index.css';

import App from "./App";
import {CookiesProvider} from "react-cookie";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import AuthProvider from "./components/AuthProvider";

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            refetchOnWindowFocus: false,
            retry: 3,
            staleTime: 0,
        },
    },
});

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <CookiesProvider defaultSetOptions={{path: '/'}}>
            <QueryClientProvider client={queryClient}>
                <AuthProvider>
                    <App/>
                </AuthProvider>
            </QueryClientProvider>
        </CookiesProvider>
    </StrictMode>
);
