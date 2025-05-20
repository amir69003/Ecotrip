package fr.ecotrip.backend.security.jwt;

import fr.ecotrip.backend.security.UserPrincipalAuthentificationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthentificationFilter extends OncePerRequestFilter {
    private final JwtDecoder decoder;

    private final JwtToPrincipalConverter jwtToPrincipalConverter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        extractTokenFromRequest(request)
                .map(decoder::decode)
                .map(jwtToPrincipalConverter::convert)
                .map(UserPrincipalAuthentificationToken::new)
                .ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/") ||
                path.equals("/auth/login") ||
                path.equals("/auth/register") ||
                path.matches("/trajets/\\d+/\\d+");
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring(7));
        }

        token = Arrays.stream(request.getCookies()).map(cookie -> cookie.getName().equals("access_token") ? cookie.getValue() : null).findFirst().orElse(null);
        if (StringUtils.hasText(token)) {
            return Optional.of(token);
        }

        return Optional.empty();
    }
}
