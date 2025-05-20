package fr.ecotrip.backend.dto;

import fr.ecotrip.backend.model.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

class UserRequestTest {

    @Test
    void testBuilderAndFields() {
        Set<Role> roles = Set.of(Role.USER);
        
        UserRequest dto = UserRequest.builder()
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .roles(roles)
                .build();

        assertNotNull(dto);
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("testuser", dto.getUsername());
        assertEquals("password", dto.getPassword());
        assertEquals(roles, dto.getRoles());
    }
}
