package fr.ecotrip.backend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class LoginResponseTest {

    @Test
    void testBuilderAndFields() {
        List<String> roles = List.of("ROLE_USER");
        
        LoginResponse dto = LoginResponse.builder()
                .accessToken("token123")
                .username("testuser")
                .email("test@example.com")
                .roles(roles)
                .build();

        assertNotNull(dto);
        assertEquals("token123", dto.getAccessToken());
        assertEquals("testuser", dto.getUsername());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals(roles, dto.getRoles());
    }
}

