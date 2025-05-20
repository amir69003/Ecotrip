package fr.ecotrip.backend.dto;

import fr.ecotrip.backend.model.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Set;

/**
 * DTO représentant la requête pour la création ou la modification d'un utilisateur.
 * Cette classe contient les informations nécessaires pour créer ou modifier un utilisateur.
 */
@Data
@Getter
@Builder
public class UserRequest {
    private String email;
    private String username;
    private String password;
    private Set<Role> roles;
}
