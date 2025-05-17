package fr.ecotrip.backend.dto;

import fr.ecotrip.backend.model.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Set;

@Data
@Getter
@Builder
public class UserRequest {
    private String email;
    private String username;
    private String password;
    private Set<Role> roles;
}
