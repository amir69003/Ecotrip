package fr.ecotrip.backend.Security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Étend AbstractAuthenticationToken pour gérer l'authentification
 * avec les informations spécifiques de l'utilisateur.
 * Cette classe est utilisée pour stocker et gérer les informations d'authentification
 * d'un utilisateur après une authentification réussie.
 */
public class UserPrincipalAuthentificationToken extends AbstractAuthenticationToken {

    private final UserPrincipal principal;
    public UserPrincipalAuthentificationToken(UserPrincipal principal) {
        super(principal.getAuthorities());
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UserPrincipal getPrincipal() {
        return principal;
    }
}
