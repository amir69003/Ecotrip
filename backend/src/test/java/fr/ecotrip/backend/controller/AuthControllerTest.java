package fr.ecotrip.backend.controller;

import fr.ecotrip.backend.dto.LoginRequest;
import fr.ecotrip.backend.dto.LoginResponse;
import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.security.UserPrincipal;
import fr.ecotrip.backend.security.jwt.JwtIssuer;
import fr.ecotrip.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private JwtIssuer jwtIssuer;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ShouldReturnLoginResponse() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@test.com")
                .password("password")
                .build();

        UserPrincipal userPrincipal = UserPrincipal.builder()
                .userId(1L)
                .email("test@test.com")
                .username("testuser")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(jwtIssuer.issue(anyLong(), anyString(), anyList()))
                .thenReturn("test.jwt.token");

        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
        
        LoginResponse loginResponse = response.getBody();
        assertNotNull(loginResponse);
        assertEquals("test.jwt.token", loginResponse.getAccessToken());
        assertEquals("testuser", loginResponse.getUsername());
        assertEquals("test@test.com", loginResponse.getEmail());
        assertEquals(Collections.singletonList("ROLE_USER"), loginResponse.getRoles());

        verify(authenticationManager).authenticate(any());
        verify(jwtIssuer).issue(anyLong(), anyString(), anyList());
    }

    @Test
    void register_ShouldCreateUserAndReturnLoginResponse() {
        UserRequest userRequest = UserRequest.builder()
                .email("new@test.com")
                .password("password")
                .username("newuser")
                .build();

        UserPrincipal userPrincipal = UserPrincipal.builder()
                .userId(1L)
                .email("new@test.com")
                .username("newuser")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(jwtIssuer.issue(anyLong(), anyString(), anyList()))
                .thenReturn("test.jwt.token");

        ResponseEntity<LoginResponse> response = authController.register(userRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
        
        LoginResponse loginResponse = response.getBody();
        assertNotNull(loginResponse);
        assertEquals("test.jwt.token", loginResponse.getAccessToken());
        assertEquals("newuser", loginResponse.getUsername());
        assertEquals("new@test.com", loginResponse.getEmail());
        assertEquals(Collections.singletonList("ROLE_USER"), loginResponse.getRoles());

        verify(userService).createUser(userRequest);
        verify(authenticationManager).authenticate(any());
        verify(jwtIssuer).issue(anyLong(), anyString(), anyList());
    }
} 