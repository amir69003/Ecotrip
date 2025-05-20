package fr.ecotrip.backend.service;

import fr.ecotrip.backend.exeption.InvalidTransportException;
import fr.ecotrip.backend.model.Co2;
import fr.ecotrip.backend.repository.Co2Repository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Co2ServiceTest {

    @Mock
    private Co2Repository co2Repository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private Co2Service co2Service;

    private Co2 voiture;
    private Co2 velo;
    private Co2 bus;

    @BeforeEach
    void setUp() {
        voiture = Co2.builder()
                .id(1L)
                .transport("Voiture thermique")
                .kco2(0.218)
                .build();

        velo = Co2.builder()
                .id(2L)
                .transport("Vélo (électrique)")
                .kco2(0.011)
                .build();

        bus = Co2.builder()
                .id(3L)
                .transport("Bus")
                .kco2(0.113)
                .build();
    }

    @Test
    void getKco2_shouldCalculateCorrectlyForVoiture() {
        when(co2Repository.findById(1L)).thenReturn(Optional.of(voiture));
        
        Double result = co2Service.getKco2(1L, 100.0f);
        
        assertEquals(21.8, result, 0.0001);
    }

    @Test
    void getKco2_shouldCalculateCorrectlyForVelo() {
        when(co2Repository.findById(2L)).thenReturn(Optional.of(velo));
        
        Double result = co2Service.getKco2(2L, 100.0f);
        
        assertEquals(1.1, result, 0.0001);
    }

    @Test
    void getKco2_shouldCalculateCorrectlyForBus() {
        when(co2Repository.findById(3L)).thenReturn(Optional.of(bus));
        
        Double result = co2Service.getKco2(3L, 100.0f);
        
        assertEquals(11.3, result, 0.0001);
    }

    @Test
    void getKco2_shouldThrowForInvalidTransport() {
        when(co2Repository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(InvalidTransportException.class, () -> co2Service.getKco2(999L, 100.0f));
    }

    @Test
    void initCo2_shouldInitializeAllTransports() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(mock(jakarta.persistence.Query.class));
        
        co2Service.initCo2();
        
        verify(co2Repository).deleteAll();
        verify(co2Repository, times(9)).save(any(Co2.class));
    }
} 