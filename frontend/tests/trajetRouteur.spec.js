import axios from 'axios';
import { pathLocal, path } from './config.js';

let token = null;

describe("API Authentication and User Retrieval", () => {
    it("should login the user and return an access token", async () => {
        try {
            const response = await axios.post(`${path}api/auth/login`, {
                email: "jasmine@test.com",
                username: "jasmine",
                password: "jasmine"
            });

            expect(response.status).toBe(200);
            expect(response.data).toBeDefined();
            expect(response.data.accessToken).toBeDefined();

            token = response.data.accessToken;
            console.log("Access Token:", token);
        } catch (error) {
            fail("Login failed: " + error.message);
        }
    });

    it("should retrieve all trajets with a valid token", async () => {
        try {
            expect(token).not.toBeNull();

            const response = await axios.get(`${path}api/trajets`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            expect(response.status).toBe(200);
            expect(Array.isArray(response.data)).toBeTrue();

            console.log("All trajets:", response.data);
        } catch (error) {
            fail("Fetching all trajets failed: " + (error.response?.data?.message || error.message));
        }
    });

    it("should retrieve one trajet with a valid token", async () => {
        try {
            expect(token).not.toBeNull();

            const response = await axios.get(`${path}api/trajets/1`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            expect(response.status).toBe(200);

            console.log("trajet:", response.data);
        } catch (error) {
            fail("Fetching one trajet failed: " + (error.response?.data?.message || error.message));
        }
    });

    it("should create a new trajet and then delete it", async () => {
        try {
            expect(token).not.toBeNull();

            // Étape 1 : Créer le trajet
            const trajetPayload = {
                depart: "Vaulx",
                arrivee: "Lyon",
                moyenTransport: "Voiture",
                kco2: 100
            };

            const createResponse = await axios.post(`${path}api/trajets`, trajetPayload, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            expect(createResponse.status).toBe(201);
            expect(createResponse.data).toBe("Trajet créé avec succès.");

            // Étape 2 : Récupérer tous les trajets
            const getResponse = await axios.get(`${path}api/trajets`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            expect(getResponse.status).toBe(200);
            expect(Array.isArray(getResponse.data)).toBeTrue();

            // Étape 3 : Trouver le dernier trajet correspondant à celui qu'on vient de créer
            const matchingTrajets = getResponse.data.filter(t =>
                t.depart === trajetPayload.depart &&
                t.arrivee === trajetPayload.arrivee &&
                t.moyenTransport === trajetPayload.moyenTransport
            );

            expect(matchingTrajets.length).toBeGreaterThan(0);

            // Récupérer le dernier ajouté (avec l'ID le plus élevé)
            const lastCreatedTrajet = matchingTrajets.reduce((prev, curr) => (curr.id > prev.id ? curr : prev));

            const trajetId = lastCreatedTrajet.id;
            console.log("Trajet créé avec ID:", trajetId);

            // Étape 4 : Supprimer ce trajet
            const deleteResponse = await axios.delete(`${path}api/trajets/${trajetId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            expect(deleteResponse.status).toBe(200);
            expect(deleteResponse.data).toBe("Trajet supprimé avec succès.");

            console.log("Delete response:", deleteResponse.data);

        } catch (error) {
            fail("Trajet creation and deletion failed: " + (error.response?.data?.message || error.message));
        }
    });
})