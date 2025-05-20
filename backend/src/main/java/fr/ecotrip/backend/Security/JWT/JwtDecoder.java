package fr.ecotrip.backend.Security.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Composant chargé de décoder les tokens JWT.
 * Utilise la bibliothèque Auth0 pour effectuer la vérification à l'aide de l'algorithme HMAC256
 * et la clé secrète définie dans les propriétés de l'application.
 */
@Component
@RequiredArgsConstructor
public class JwtDecoder {

    private final JwtProperties jwtProperties;

    /**
     * Décode un token JWT à l'aide de la clé secrète.
     *
     * @param token Le token JWT sous forme de chaîne
     * @return Un objet {@link DecodedJWT} représentant les informations contenues dans le token
     */
    public DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                .build()
                .verify(token);
    }
}
