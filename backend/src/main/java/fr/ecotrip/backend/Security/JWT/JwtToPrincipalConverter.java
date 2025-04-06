package fr.ecotrip.backend.Security.JWT;


import com.auth0.jwt.interfaces.DecodedJWT;
import fr.ecotrip.backend.Security.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtToPrincipalConverter {

    public UserPrincipal convert(DecodedJWT jwt) {
        return UserPrincipal.builder()
                .userId(Long.valueOf(jwt.getSubject()))
                .email(String.valueOf(jwt.getClaim("email")))
                .authorities(extractAuthFromClaim(jwt))
                .build();
    }


    private List<SimpleGrantedAuthority> extractAuthFromClaim(DecodedJWT jwt) {
        var claim = jwt.getClaim("role");
        if (claim.isNull() || claim.isMissing()) return List.of();
        return claim.asList(SimpleGrantedAuthority.class);
    }
}
