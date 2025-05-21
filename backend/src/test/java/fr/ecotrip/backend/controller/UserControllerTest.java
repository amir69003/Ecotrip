package fr.ecotrip.backend.controller;

import fr.ecotrip.backend.dto.TrajetsResponse;
import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.dto.UserResponse;
import fr.ecotrip.backend.model.Role;
import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.security.UserPrincipal;
import fr.ecotrip.backend.service.TrajetService;
import fr.ecotrip.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TrajetService trajetService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        List<UserResponse> expectedUsers = List.of(
            UserResponse.builder()
                .email("user1@test.com")
                .username("user1")
                .roles(Set.of(Role.USER))
                .build(),
            UserResponse.builder()
                .email("user2@test.com")
                .username("user2")
                .roles(Set.of(Role.USER))
                .build()
        );

        when(userService.findAll()).thenReturn(expectedUsers);

        ResponseEntity<List<UserResponse>> response = userController.getAllUsers();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedUsers, response.getBody());
        verify(userService).findAll();
    }

    @Test
    void getUser_ShouldReturnUser() {
        Long userId = 1L;
        UserResponse expectedUser = UserResponse.builder()
            .email("test@test.com")
            .username("testuser")
            .roles(Set.of(Role.USER))
            .build();

        when(userService.findUser(userId)).thenReturn(expectedUser);

        ResponseEntity<UserResponse> response = userController.getUser(userId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedUser, response.getBody());
        verify(userService).findUser(userId);
    }

    @Test
    void updateUser_ShouldUpdateUser() {
        Long userId = 1L;
        UserRequest updateRequest = UserRequest.builder()
            .email("updated@test.com")
            .username("updateduser")
            .password("newpassword")
            .build();

        ResponseEntity<Void> response = userController.updateUser(userId, updateRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(userService).updateUser(userId, updateRequest);
    }

    @Test
    void getTrajetsFromUser_ShouldReturnUserTrajets() {
        UserPrincipal userPrincipal = UserPrincipal.builder()
            .userId(1L)
            .email("test@test.com")
            .username("testuser")
            .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
            .build();

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities())
        );

        List<Trajet> expectedTrajets = List.of(
            Trajet.builder()
                .depart("Paris")
                .arrivee("Lyon")
                .moyenTransport("Train")
                .build()
        );

        when(trajetService.findByUserId(anyLong())).thenReturn(expectedTrajets);

        ResponseEntity<TrajetsResponse> response = userController.getTrajetsFromUser();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedTrajets, response.getBody().getTrajets());
        verify(trajetService).findByUserId(userPrincipal.getUserId());
    }
} 