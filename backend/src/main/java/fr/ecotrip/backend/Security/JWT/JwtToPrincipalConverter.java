package fr.ecotrip.backend.Security.JWT;

import com.auth0.jwt.interfaces.DecodedJWT;
import fr.ecotrip.backend.Security.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Composant chargé de convertir un token JWT décodé en un objet {@link UserPrincipal}.
 * <p>
 * Il extrait les informations nécessaires du token, telles que l'ID utilisateur, l'email,
 * et les rôles, puis les encapsule dans une instance de {@link UserPrincipal}.
 * </p>
 */
@Component
public class JwtToPrincipalConverter {

    /**
     * Convertit un JWT décodé en un {@link UserPrincipal}.
     *
     * @param jwt Le token JWT décodé
     * @return Un {@link UserPrincipal} contenant les données extraites du token
     */
    public UserPrincipal convert(DecodedJWT jwt) {
        return UserPrincipal.builder()
                .userId(Long.valueOf(jwt.getSubject()))
                .email(String.valueOf(jwt.getClaim("email")))
                .authorities(extractAuthFromClaim(jwt))
                .build();
    }

    /**
     * Extrait la liste des rôles (autorisations) depuis la claim "role" du JWT.
     * <p>
     * Si la claim est absente ou nulle, une liste vide est retournée.
     * </p>
     *
     * @param jwt Le token JWT décodé
     * @return Une liste de {@link SimpleGrantedAuthority}
     */
    private List<SimpleGrantedAuthority> extractAuthFromClaim(DecodedJWT jwt) {
        var claim = jwt.getClaim("role");
        if (claim.isNull() || claim.isMissing()) return List.of();
        return claim.asList(SimpleGrantedAuthority.class);
    }
}
