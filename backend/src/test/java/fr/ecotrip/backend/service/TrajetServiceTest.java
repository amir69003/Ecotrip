package fr.ecotrip.backend.service;

import fr.ecotrip.backend.exeption.ForbiddenActionException;
import fr.ecotrip.backend.exeption.NoTrajetsFoundException;
import fr.ecotrip.backend.exeption.UnauthenticatedUserException;
import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.model.User;
import fr.ecotrip.backend.repository.TrajetRepository;
import fr.ecotrip.backend.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrajetServiceTest {

    @Mock
    private TrajetRepository trajetRepository;

    @InjectMocks
    private TrajetService trajetService;

    private User testUser;
    private Trajet testTrajet;
    private UserPrincipal userPrincipal;
    private Authentication authentication;

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

        authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());
    }

    @Test
    void findAll_shouldReturnAllTrajets() {
        when(trajetRepository.findAll()).thenReturn(Arrays.asList(testTrajet));
        List<Trajet> trajets = trajetService.findAll();
        assertEquals(1, trajets.size());
        assertEquals(testTrajet, trajets.get(0));
    }

    @Test
    void findOne_shouldReturnTrajet() {
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(testTrajet));
        Trajet found = trajetService.findOne(1L);
        assertEquals(testTrajet, found);
    }

    @Test
    void findOne_shouldThrowIfNotFound() {
        when(trajetRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NoTrajetsFoundException.class, () -> trajetService.findOne(2L));
    }

    @Test
    void createTrajet_shouldSaveTrajet() {
        trajetService.createTrajet(testTrajet);
        verify(trajetRepository).save(testTrajet);
    }

    @Test
    void findByUserId_shouldReturnUserTrajets() {
        when(trajetRepository.findAllByUserId(1L)).thenReturn(Arrays.asList(testTrajet));
        List<Trajet> trajets = trajetService.findByUserId(1L);
        assertEquals(1, trajets.size());
        assertEquals(testTrajet, trajets.get(0));
    }

    @Test
    void findByIdTrajet_shouldReturnTrajet() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(testTrajet));
        
        Trajet found = trajetService.findByIdTrajet(1L);
        assertEquals(testTrajet, found);
    }

    @Test
    void findByIdTrajet_shouldThrowIfNotAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);
        assertThrows(UnauthenticatedUserException.class, () -> trajetService.findByIdTrajet(1L));
    }

    @Test
    void findByIdTrajet_shouldThrowIfNotFound() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(trajetRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NoTrajetsFoundException.class, () -> trajetService.findByIdTrajet(2L));
    }

    @Test
    void deleteById_shouldDeleteTrajet() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(testTrajet));
        
        trajetService.deleteById(1L);
        verify(trajetRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrowIfNotAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);
        assertThrows(UnauthenticatedUserException.class, () -> trajetService.deleteById(1L));
    }

    @Test
    void deleteById_shouldThrowIfDeletingOtherUserTrajet() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User otherUser = User.builder()
                .id(2L)
                .username("otheruser")
                .email("other@example.com")
                .build();
        
        Trajet otherTrajet = Trajet.builder()
                .id(2L)
                .user(otherUser)
                .build();
        
        when(trajetRepository.findById(2L)).thenReturn(Optional.of(otherTrajet));
        
        assertThrows(ForbiddenActionException.class, () -> trajetService.deleteById(2L));
    }
} 