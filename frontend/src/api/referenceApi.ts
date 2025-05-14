const fakeReferenceValues = {
    voiture: 0.2,
    transport: 0.1,
    bateau: 0.3,
    velo: 0.01,
    pied: 0.0
};

export async function fetchReferenceValues(useFakeData: boolean) {
    if (useFakeData) {
        return Promise.resolve(fakeReferenceValues);
    } else {
        const response = await fetch("https://api.example.com/reference-values");
        if (!response.ok) throw new Error("Erreur lors du chargement des valeurs de référence");
        return response.json();
    }
}

export async function updateReferenceValues(values: typeof fakeReferenceValues, useFakeData: boolean) {
    if (useFakeData) {
        // Simule un POST
        return Promise.resolve({ success: true, values });
    } else {
        const response = await fetch("https://api.example.com/reference-values", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(values)
        });
        if (!response.ok) throw new Error("Erreur lors de la mise à jour des valeurs de référence");
        return response.json();
    }
} 