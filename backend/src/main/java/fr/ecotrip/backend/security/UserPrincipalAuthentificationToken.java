package fr.ecotrip.backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;


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
