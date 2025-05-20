package fr.ecotrip.backend.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserResponseTest {

    @Test
    void testBuilderAndGetters() {
        UserResponse dto = UserResponse.builder()
                .email("test@example.com")
                .username("testuser")
                .build();

        assertEquals("test@example.com", dto.getEmail());
        assertEquals("testuser", dto.getUsername());
    }
}
