package fr.ecotrip.backend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrajetRequestTest {

    @Test
    void testBuilderAndFields() {
        TrajetRequest dto = TrajetRequest.builder()
                .depart("Lyon")
                .arrivee("Paris")
                .moyenTransport("Train")
                .kCo2(4.0)
                .build();

        assertEquals("Lyon", dto.getDepart());
        assertEquals("Paris", dto.getArrivee());
        assertEquals("Train", dto.getMoyenTransport());
        assertEquals(4.0, dto.getKCo2());
    }
}
