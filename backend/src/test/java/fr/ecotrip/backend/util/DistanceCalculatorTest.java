package fr.ecotrip.backend.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistanceCalculatorTest {

    @Test
    void calculateDistance_samePoint_shouldReturnZero() {
        double lat = 48.8566;
        double lon = 2.3522;
        double distance = DistanceCalculator.calculateDistance(lat, lon, lat, lon);
        assertEquals(0.0, distance, 0.0001);
    }

    @Test
    void calculateDistance_parisToLyon_shouldReturnApprox392km() {
        double parisLat = 48.8566;
        double parisLon = 2.3522;
        double lyonLat = 45.7578;
        double lyonLon = 4.8320;
        double distance = DistanceCalculator.calculateDistance(parisLat, parisLon, lyonLat, lyonLon);
        assertEquals(392, distance, 2);
    }
} 