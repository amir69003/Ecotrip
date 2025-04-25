package fr.ecotrip.backend.Security.JWT;

import fr.ecotrip.backend.Security.UserPrincipalAuthentificationToken;
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
import java.util.Optional;

/**
 * Filtre d'authentification JWT pour les requêtes HTTP.
 * <p>
 * Ce filtre est exécuté une seule fois par requête (grâce à {@link OncePerRequestFilter}).
 * Il extrait le token JWT depuis l'en-tête "Authorization", le décode, puis crée
 * un {@link UserPrincipalAuthentificationToken} qui est injecté dans le {@link SecurityContextHolder}.
 * </p>
 *
 * <p>Utilise {@link JwtDecoder} pour décoder le JWT et {@link JwtToPrincipalConverter} pour
 * transformer le JWT en principal utilisateur.</p>
 *
 */
@Component
@RequiredArgsConstructor
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    /**
     * Composant chargé de décoder un token JWT en objet Jwt.
     */
    private final JwtDecoder decoder;

    /**
     * Convertisseur de JWT en objet principal utilisable pour l'authentification Spring Security.
     */
    private final JwtToPrincipalConverter jwtToPrincipalConverter;

    /**
     * Exécute le filtre pour chaque requête HTTP.
     * <p>
     * Ce filtre tente d'extraire un token JWT depuis la requête,
     * puis d'en extraire un principal utilisateur et de l'ajouter au contexte de sécurité.
     * </p>
     *
     * @param request     La requête HTTP entrante
     * @param response    La réponse HTTP
     * @param filterChain Le reste de la chaîne de filtres
     * @throws ServletException En cas d'erreur liée à la servlet
     * @throws IOException      En cas d'erreur d'entrée/sortie
     */
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

    /**
     * Extrait le token JWT du header "Authorization" de la requête HTTP.
     * <p>
     * Le token doit être au format "Bearer [token]". Si ce n'est pas le cas, un {@link Optional} vide est retourné.
     * </p>
     *
     * @param request La requête HTTP contenant éventuellement un header Authorization
     * @return Un {@link Optional} contenant le token sans le préfixe "Bearer ", ou vide si invalide/absent
     */
    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }
}
