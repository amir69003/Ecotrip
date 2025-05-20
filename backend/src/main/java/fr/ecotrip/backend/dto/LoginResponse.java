package fr.ecotrip.backend.dto;

import lombok.Getter;
import lombok.Builder;

/**
 * DTO représentant la réponse d'authentification.
 * Cette classe contient le token d'accès généré après une authentification réussie.
 */
@Getter
@Builder
public class LoginResponse {

    private final String accessToken;

}
