package fr.ecotrip.backend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    @Test
    void testBuilderAndFields() {
        LoginResponse dto = LoginResponse.builder()
                .accessToken("token123")
                .build();

        assertEquals("token123", dto.getAccessToken());
    }
}

