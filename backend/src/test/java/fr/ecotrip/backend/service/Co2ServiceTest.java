package fr.ecotrip.backend.service;

import fr.ecotrip.backend.model.Co2;
import fr.ecotrip.backend.repository.Co2Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Co2ServiceTest {

    @Mock
    private Co2Repository co2Repository;

    @InjectMocks
    private Co2Service co2Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetKco2_WhenIdExists() {
        Long transportId = 1L;
        double kco2 = 0.104; // ex: 104 g/km → 0.104 kg/km
        float km = 10f;

        Co2 co2 = Co2.builder().kco2(kco2).transport("bus").build();
        when(co2Repository.findById(transportId)).thenReturn(Optional.of(co2));

        Double result = co2Service.getKco2(transportId, km);

        assertEquals(kco2 * km, result);
    }

    @Test
    void testGetKco2_WhenIdDoesNotExist() {
        Long transportId = 999L;
        float km = 10f;

        when(co2Repository.findById(transportId)).thenReturn(Optional.empty());

        Double result = co2Service.getKco2(transportId, km);

        assertEquals(-1.0 * km, result);
    }

    @Test
    void testInitCo2_SavesAllModes() {
        co2Service.initCo2();

        verify(co2Repository, times(6)).save(any(Co2.class));
    }
}
