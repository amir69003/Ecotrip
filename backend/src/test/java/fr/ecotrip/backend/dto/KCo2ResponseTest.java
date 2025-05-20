package fr.ecotrip.backend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KCo2ResponseTest {

    @Test
    void testBuilderAndGetter() {
        double kCo2Value = 42.0;
        KCo2Response response = KCo2Response.builder()
                .kCo2(kCo2Value)
                .build();

        assertNotNull(response);
        assertEquals(kCo2Value, response.getKCo2());
    }
}
