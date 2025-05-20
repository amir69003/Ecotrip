package fr.ecotrip.backend.Security.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Composant responsable de la génération de tokens JWT.
 * Ce composant crée des tokens contenant l'identifiant utilisateur, l'email et les rôles.
 * Les tokens sont signés avec une clé secrète en utilisant l'algorithme HMAC256.
 */
@Component
@RequiredArgsConstructor
public class JwtIssuer {

    /**
     * Propriétés de configuration du JWT, incluant la clé secrète pour signer le token.
     */
    private final JwtProperties jwtProperties;

    /**
     * Génère un token JWT avec les informations de l'utilisateur.
     * Il expire après 24 heures.
     *
     * @param userId L'identifiant unique de l'utilisateur
     * @param email  L'adresse email de l'utilisateur
     * @param role   La liste des rôles attribués à l'utilisateur
     * @return Une chaîne représentant le token JWT signé
     */
    public String issue(long userId, String email, List<String> role) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS)))
                .withClaim("email", email)
                .withClaim("role", role)
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }
}
