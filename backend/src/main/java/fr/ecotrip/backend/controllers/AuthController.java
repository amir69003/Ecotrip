package fr.ecotrip.backend.controllers;

import fr.ecotrip.backend.Security.JWT.JwtIssuer;
import fr.ecotrip.backend.Security.UserPrincipal;
import fr.ecotrip.backend.dto.LoginRequest;
import fr.ecotrip.backend.dto.LoginResponse;
import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtIssuer jwtIssuer;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserRequest user) {
        try {
            userService.createUser(user);
            return ResponseEntity.ok(authenticateAndGenerateToken(user.getEmail(), user.getPassword()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Un utilisateur avec cet email ou username existe déjà.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de l'enregistrement.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated LoginRequest request) {
        try {
            return ResponseEntity.ok(authenticateAndGenerateToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou mot de passe incorrect.");
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Le compte est désactivé.");
        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body("Le compte est verrouillé.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de la connexion.");
        }
    }

    private LoginResponse authenticateAndGenerateToken(String email, String password) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        var roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);

        return LoginResponse.builder().accessToken(token).build();
    }
}
