package fr.ecotrip.backend.controller;

import fr.ecotrip.backend.dto.KCo2Response;
import fr.ecotrip.backend.dto.TrajetCommunResponse;
import fr.ecotrip.backend.dto.TrajetRequest;
import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.model.User;
import fr.ecotrip.backend.security.UserPrincipal;
import fr.ecotrip.backend.service.Co2Service;
import fr.ecotrip.backend.service.TrajetService;
import fr.ecotrip.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrajetControllerTest {

    @Mock
    private TrajetService trajetService;

    @Mock
    private UserService userService;

    @Mock
    private Co2Service co2Service;

    @InjectMocks
    private TrajetController trajetController;

    private User testUser;
    private Trajet testTrajet;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        testTrajet = Trajet.builder()
                .id(1L)
                .depart("Paris")
                .departLatitude(48.8566)
                .departLongitude(2.3522)
                .arrivee("Lyon")
                .arriveeLatitude(45.7578)
                .arriveeLongitude(4.8320)
                .moyenTransport("voiture")
                .kCo2(100.0)
                .user(testUser)
                .build();

        userPrincipal = UserPrincipal.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getAllTrajets_ShouldReturnListOfTrajets() {
        List<Trajet> expectedTrajets = Arrays.asList(testTrajet);
        when(trajetService.findAll()).thenReturn(expectedTrajets);

        ResponseEntity<List<Trajet>> response = trajetController.getAllTrajets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTrajets, response.getBody());
        verify(trajetService).findAll();
    }

    @Test
    void getTrajet_ShouldReturnTrajet() {
        when(trajetService.findOne(1L)).thenReturn(testTrajet);

        ResponseEntity<Trajet> response = trajetController.getTrajet(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testTrajet, response.getBody());
        verify(trajetService).findOne(1L);
    }

    @Test
    void createTrajet_ShouldCreateNewTrajet() {
        TrajetRequest trajetRequest = TrajetRequest.builder()
                .depart("Paris")
                .departLatitude(48.8566)
                .departLongitude(2.3522)
                .arrivee("Lyon")
                .arriveeLatitude(45.7578)
                .arriveeLongitude(4.8320)
                .moyenTransport("voiture")
                .kCo2(100.0)
                .build();

        when(userService.findById(1L)).thenReturn(testUser);

        ResponseEntity<Void> response = trajetController.createTrajet(trajetRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(trajetService).createTrajet(any(Trajet.class));
    }

    @Test
    void deleteTrajet_ShouldDeleteTrajet() {
        ResponseEntity<Void> response = trajetController.deleteTrajet(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trajetService).deleteById(1L);
    }

    @Test
    void calculCo2_ShouldReturnKCo2Response() {
        Double expectedKCo2 = 50.0;
        when(co2Service.getKco2(1L, 10.0f)).thenReturn(expectedKCo2);

        ResponseEntity<KCo2Response> response = trajetController.calculCo2(1L, 10.0f);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedKCo2, response.getBody().getKCo2());
        verify(co2Service).getKco2(1L, 10.0f);
    }

    @Test
    void getTrajetsCommuns_ShouldReturnListOfTrajetsCommuns() {
        List<TrajetCommunResponse> expectedTrajetsCommuns = Arrays.asList(
            TrajetCommunResponse.builder()
                .trajet(testTrajet)
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .distanceDepart(0.0)
                .distanceArrivee(0.0)
                .build()
        );

        when(trajetService.findOne(1L)).thenReturn(testTrajet);
        when(trajetService.findTrajetsCommuns(1L)).thenReturn(expectedTrajetsCommuns);

        ResponseEntity<List<TrajetCommunResponse>> response = trajetController.getTrajetsCommuns(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTrajetsCommuns, response.getBody());
        verify(trajetService).findTrajetsCommuns(1L);
    }

    @Test
    void getAllTrajetsCommuns_ShouldReturnListOfAllTrajetsCommuns() {
        List<TrajetCommunResponse> expectedTrajetsCommuns = Arrays.asList(
            TrajetCommunResponse.builder()
                .trajet(testTrajet)
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .distanceDepart(0.0)
                .distanceArrivee(0.0)
                .build()
        );

        when(trajetService.getAllTrajetsCommuns()).thenReturn(expectedTrajetsCommuns);

        ResponseEntity<List<TrajetCommunResponse>> response = trajetController.getAllTrajetsCommuns();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTrajetsCommuns, response.getBody());
        verify(trajetService).getAllTrajetsCommuns();
    }
} 