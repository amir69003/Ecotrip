package fr.ecotrip.backend;

import fr.ecotrip.backend.service.Co2Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private Co2Service co2Service;

    @Mock
    private ApplicationArguments args;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void whenRun_thenInitCo2IsCalled() {
        // When
        dataInitializer.run(args);

        // Then
        verify(co2Service).initCo2();
    }
} 