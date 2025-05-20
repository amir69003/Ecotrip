package fr.ecotrip.backend.dto;

import fr.ecotrip.backend.model.Trajet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrajetsResponseTest {

    @Test
    void testBuilderAndGetter() {
        Trajet trajet = Trajet.builder()
                .depart("Lyon")
                .arrivee("Paris")
                .moyenTransport("Train")
                .kCo2(4.0)
                .build();

        List<Trajet> list = List.of(trajet);

        TrajetsResponse response = TrajetsResponse.builder()
                .trajets(list)
                .build();

        assertEquals(1, response.getTrajets().size());
        assertEquals("Lyon", response.getTrajets().get(0).getDepart());
        assertEquals("Paris", response.getTrajets().get(0).getArrivee());
    }
}
