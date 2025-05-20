package fr.ecotrip.backend.dto;

import fr.ecotrip.backend.model.Trajet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrajetCommunResponseTest {

    @Test
    void testBuilderAndFields() {
        Trajet trajet = Trajet.builder()
                .depart("Lyon")
                .arrivee("Paris")
                .moyenTransport("Train")
                .kCo2(4.0)
                .build();

        TrajetCommunResponse response = TrajetCommunResponse.builder()
                .trajet(trajet)
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .distanceDepart(5.0)
                .distanceArrivee(3.0)
                .build();

        assertNotNull(response);
        assertEquals(trajet, response.getTrajet());
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(5.0, response.getDistanceDepart());
        assertEquals(3.0, response.getDistanceArrivee());
    }
} 