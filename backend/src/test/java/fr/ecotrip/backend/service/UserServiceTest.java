package fr.ecotrip.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.model.User;
import fr.ecotrip.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    public void testUpdateUser_Success() {
        Long userId = 1L;

        User existingUser = User.builder()
                .id(userId)
                .email("old@example.com")
                .username("old_username")
                .password("oldpass")
                .build();

        UserRequest request = UserRequest.builder()
                .email("new@example.com")
                .username("new_username")
                .password("newpass123")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.updateUser(userId, request);

        assertEquals("new@example.com", existingUser.getEmail());
        assertEquals("new_username", existingUser.getUsername());
        assertTrue(passwordEncoder.matches("newpass123", existingUser.getPassword()));
    }
}
