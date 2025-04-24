import {useState} from "react";

type ItineraryData = {
    departure: string,
    arrival: string,
}
export function useDestinationForm( onSubmit: (data :ItineraryData) => void) {
    const [itineraryData, setItineraryData] = useState<ItineraryData>({
        departure:'',
        arrival:''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name , value } = e.target;
        setItineraryData(prevData => ({...prevData, [name]: value}));
    }


    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        onSubmit(itineraryData);
    }

    return {itineraryData, handleChange, handleSubmit};
}