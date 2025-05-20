package fr.ecotrip.backend.dto;

import fr.ecotrip.backend.model.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Set;

/**
 * DTO représentant la réponse pour les informations d'un utilisateur.
 * Cette classe contient les informations essentielles d'un utilisateur, incluant son email,
 * son nom d'utilisateur et ses rôles dans le système.
 */
@Data
@Getter
@Builder
public class UserResponse {
    private String email;
    private String username;
    private Set<Role> roles;
}
