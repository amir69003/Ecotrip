package fr.ecotrip.backend.dto;

import fr.ecotrip.backend.model.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

class UserResponseTest {

    @Test
    void testBuilderAndGetters() {
        Set<Role> roles = Set.of(Role.USER);
        
        UserResponse dto = UserResponse.builder()
                .email("test@example.com")
                .username("testuser")
                .roles(roles)
                .build();

        assertNotNull(dto);
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("testuser", dto.getUsername());
        assertEquals(roles, dto.getRoles());
    }
}
