package fr.ecotrip.backend.service;

import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.dto.UserResponse;
import fr.ecotrip.backend.exeption.ForbiddenActionException;
import fr.ecotrip.backend.exeption.UnauthenticatedUserException;
import fr.ecotrip.backend.exeption.UserNotFoundException;
import fr.ecotrip.backend.model.Role;
import fr.ecotrip.backend.model.User;
import fr.ecotrip.backend.repository.UserRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserPrincipal userPrincipal;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .roles(Set.of(Role.USER))
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
    void findAll_shouldReturnUserResponses() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserResponse> users = userService.findAll();
        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
    }

    @Test
    void findUser_shouldReturnUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserResponse response = userService.findUser(1L);
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void findUser_shouldThrowIfNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.findUser(2L));
    }

    @Test
    void findByEmail_shouldReturnUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        User found = userService.findByEmail("test@example.com");
        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
    }

    @Test
    void createUser_shouldSaveUser() {
        UserRequest request = UserRequest.builder()
                .email("new@example.com")
                .username("newuser")
                .password("password")
                .roles(Set.of(Role.USER))
                .build();
        when(userRepository.findByEmail("new@example.com")).thenReturn(null);
        when(userRepository.findByUsername("newuser")).thenReturn(null);
        userService.createUser(request);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowIfEmailExists() {
        UserRequest request = UserRequest.builder()
                .email("test@example.com")
                .username("newuser")
                .password("password")
                .roles(Set.of(Role.USER))
                .build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        assertThrows(ForbiddenActionException.class, () -> userService.createUser(request));
    }

    @Test
    void createUser_shouldThrowIfUsernameExists() {
        UserRequest request = UserRequest.builder()
                .email("new@example.com")
                .username("testuser")
                .password("password")
                .roles(Set.of(Role.USER))
                .build();
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        assertThrows(ForbiddenActionException.class, () -> userService.createUser(request));
    }

    @Test
    void createUser_shouldAllowAdminCreationByAdmin() {
        UserRequest request = UserRequest.builder()
                .email("admin@example.com")
                .username("adminuser")
                .password("password")
                .roles(Set.of(Role.ADMIN))
                .build();

        UserPrincipal adminPrincipal = UserPrincipal.builder()
                .userId(2L)
                .username("admin")
                .email("admin@admin.com")
                .password("password")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();

        Authentication adminAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                adminPrincipal, null, adminPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(adminAuth);

        when(userRepository.findByEmail("admin@example.com")).thenReturn(null);
        when(userRepository.findByUsername("adminuser")).thenReturn(null);

        userService.createUser(request);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_shouldPreventAdminCreationByNonAdmin() {
        UserRequest request = UserRequest.builder()
                .email("admin@example.com")
                .username("adminuser")
                .password("password")
                .roles(Set.of(Role.ADMIN))
                .build();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("admin@example.com")).thenReturn(null);
        when(userRepository.findByUsername("adminuser")).thenReturn(null);

        assertThrows(ForbiddenActionException.class, () -> userService.createUser(request));
    }

    @Test
    void findById_shouldReturnUser() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        User found = userService.findById(1L);
        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
    }

    @Test
    void findById_shouldThrowIfNotAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);
        assertThrows(UnauthenticatedUserException.class, () -> userService.findById(1L));
    }

    @Test
    void findById_shouldThrowIfUserNotFound() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findById(2L));
    }

    @Test
    void updateUser_shouldUpdateUser() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("new@example.com")).thenReturn(null);
        when(userRepository.findByUsername("newuser")).thenReturn(null);

        UserRequest request = UserRequest.builder()
                .email("new@example.com")
                .username("newuser")
                .password("newpassword")
                .build();

        userService.updateUser(1L, request);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_shouldThrowIfNotAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);
        UserRequest request = UserRequest.builder()
                .email("new@example.com")
                .username("newuser")
                .build();

        assertThrows(UnauthenticatedUserException.class, () -> userService.updateUser(1L, request));
    }

    @Test
    void updateUser_shouldThrowIfUpdatingOtherUser() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        UserRequest request = UserRequest.builder()
                .email("new@example.com")
                .username("newuser")
                .build();

        assertThrows(ForbiddenActionException.class, () -> userService.updateUser(2L, request));
    }

    @Test
    void updateUser_shouldThrowIfEmailExists() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User existingUser = User.builder()
                .id(2L)
                .email("existing@example.com")
                .username("existinguser")
                .build();

        when(userRepository.findByEmail("existing@example.com")).thenReturn(existingUser);

        UserRequest request = UserRequest.builder()
                .email("existing@example.com")
                .username("newuser")
                .build();

        assertThrows(ForbiddenActionException.class, () -> userService.updateUser(1L, request));
    }
} 