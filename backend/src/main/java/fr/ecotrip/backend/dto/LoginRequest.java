package fr.ecotrip.backend.dto;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;

/**
 * DTO représentant la requête d'authentification.
 * Cette classe contient les informations nécessaires pour l'authentification d'un utilisateur.
 */
@Getter
@Builder
@AllArgsConstructor
public class LoginRequest {
    private final String email;
    private final String password;
}

