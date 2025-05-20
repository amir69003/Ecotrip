import {useCallback, useEffect, useRef, useState} from "react";
import {extractLieu} from "../assets/services/locationService";
import {fetchOpenStreetMapLocationSuggestions} from "../lib/openStreetMap";
import {DetailedLocation, Location} from "../model";

export function useSuggestions(query: string) {
    const [suggestions, setSuggestions] = useState<DetailedLocation[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const blurTimeout = useRef<ReturnType<typeof setTimeout> | null>(null);
    const debounceTimeout = useRef<ReturnType<typeof setTimeout> | null>(null);
    const [hasSelected, setHasSelected] = useState(false);

    const fetchSuggestions = useCallback(async () => {
        try {
            setIsLoading(true);
            const data = await fetchOpenStreetMapLocationSuggestions(query);
            setSuggestions(data);
        } catch (error) {
            console.error("Erreur fetchSuggestions:", error);
            setSuggestions([]);
        } finally {
            setIsLoading(false);
        }
    }, [query]);

    useEffect(() => {
        if (hasSelected || query.trim().length < 2) {
            setSuggestions([]);
            return;
        }

        if (debounceTimeout.current) clearTimeout(debounceTimeout.current);

        debounceTimeout.current = setTimeout(() => {
            fetchSuggestions();
        }, 500);

        return () => {
            if (debounceTimeout.current) clearTimeout(debounceTimeout.current);
        };
    }, [query, hasSelected, fetchSuggestions]);

    useEffect(() => {
        return () => {
            if (blurTimeout.current) clearTimeout(blurTimeout.current);
        };
    }, []);

    const clearSuggestions = () => {
        blurTimeout.current = setTimeout(() => {
            setSuggestions([]);
        }, 200);
    };

    const selectSuggestion = (item: DetailedLocation): Location => {
        setSuggestions([]);
        setHasSelected(true);
        return extractLieu(item);
    };

    const resetSelection = () => setHasSelected(false);

    return {
        suggestions,
        isLoading,
        selectSuggestion,
        clearSuggestions,
        resetSelection,
    };
}
