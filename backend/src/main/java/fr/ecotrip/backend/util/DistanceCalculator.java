package fr.ecotrip.backend.util;

/**
 * Utilitaire pour le calcul de distances entre deux points géographiques.
 * Utilise la formule de Haversine pour calculer la distance entre deux points
 * sur la surface de la Terre en utilisant leurs coordonnées géographiques.
 */
public class DistanceCalculator {
    private static final double EARTH_RADIUS = 6371; 

    /**
     * Calcule la distance entre deux points géographiques en utilisant la formule de Haversine.
     * @param lat1 Latitude du premier point en degrés
     * @param lon1 Longitude du premier point en degrés
     * @param lat2 Latitude du deuxième point en degrés
     * @param lon2 Longitude du deuxième point en degrés
     * @return Distance en kilomètres entre les deux points
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c;
    }
} 