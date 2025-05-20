package fr.ecotrip.backend.service;

import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.repository.TrajetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrajetServiceTest {

    @Mock
    private TrajetRepository trajetRepository;

    @InjectMocks
    private TrajetService trajetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Trajet trajet1 = new Trajet();
        Trajet trajet2 = new Trajet();
        when(trajetRepository.findAll()).thenReturn(Arrays.asList(trajet1, trajet2));

        List<Trajet> result = trajetService.findAll();

        assertEquals(2, result.size());
        verify(trajetRepository).findAll();
    }

    @Test
    void testFindOne_Found() {
        Trajet trajet = new Trajet();
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));

        Trajet result = trajetService.findOne(1L);

        assertNotNull(result);
        verify(trajetRepository).findById(1L);
    }

    @Test
    void testFindOne_NotFound() {
        when(trajetRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            trajetService.findOne(999L);
        });

        assertTrue(exception.getMessage().contains("Trajet avec l'ID 999 non trouvé."));
    }

    @Test
    void testCreateTrajet() {
        Trajet trajet = new Trajet();
        trajetService.createTrajet(trajet);

        verify(trajetRepository).save(trajet);
    }

    @Test
    void testDeleteById() {
        trajetService.deleteById(5L);

        verify(trajetRepository).deleteById(5L);
    }
}
