package fr.ecotrip.backend.controllers;

import fr.ecotrip.backend.Security.UserPrincipal;
import fr.ecotrip.backend.dto.TrajetsResponse;
import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.dto.UserResponse;
import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.service.TrajetService;
import fr.ecotrip.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TrajetService trajetService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponse> users = userService.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des utilisateurs.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            UserResponse user = userService.findUser(id);
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Utilisateur avec l'ID " + id + " non trouvé.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne lors de la récupération de l'utilisateur.");
        }
    }


    @GetMapping("/trajets")
    public ResponseEntity<?> getTrajetsFromUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication.getPrincipal() instanceof UserPrincipal principal)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Utilisateur non authentifié.");
            }

            Long userId = principal.getUserId();
            List<Trajet> trajets = trajetService.findByUserId(userId);

            return ResponseEntity.ok(
                    TrajetsResponse.builder()
                            .trajets(trajets)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des trajets.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Validated UserRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Utilisateur non authentifié.");
            }

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            if (!principal.getUserId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Vous ne pouvez modifier que votre propre compte.");
            }

            userService.updateUser(id, request);
            return ResponseEntity.ok("Utilisateur mis à jour avec succès.");

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Utilisateur avec l'ID " + id + " non trouvé.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour de l'utilisateur.");
        }
    }

}
