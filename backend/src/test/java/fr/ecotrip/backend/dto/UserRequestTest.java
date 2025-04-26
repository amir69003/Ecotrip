package fr.ecotrip.backend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserRequestTest {

    @Test
    void testBuilderAndFields() {
        UserRequest dto = UserRequest.builder()
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .build();

        assertEquals("test@example.com", dto.getEmail());
        assertEquals("testuser", dto.getUsername());
        assertEquals("password", dto.getPassword());
    }
}
