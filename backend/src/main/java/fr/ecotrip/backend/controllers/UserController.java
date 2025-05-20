package fr.ecotrip.backend.controllers;

import fr.ecotrip.backend.Security.UserPrincipal;
import fr.ecotrip.backend.dto.TrajetsResponse;
import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.dto.UserResponse;
import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.service.TrajetService;
import fr.ecotrip.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 * Routes pour créer, récupérer, mettre à jour et gérer les utilisateurs.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TrajetService trajetService;

    /**
     * Récupère tous les utilisateurs.
     * @return Liste de tous les utilisateurs
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Récupère un utilisateur par son ID.
     * @param id ID de l'utilisateur
     * @return L'utilisateur correspondant
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse user = userService.findUser(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Met à jour les informations d'un utilisateur.
     * @param id ID de l'utilisateur à mettre à jour
     * @param request Données de mise à jour
     * @return Message de confirmation
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Validated UserRequest request) {
        userService.updateUser(id, request);
        return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
    }

    /**
     * Récupère tous les trajets de l'utilisateur connecté.
     * @return Liste des trajets de l'utilisateur
     */
    @GetMapping("/trajets")
    public ResponseEntity<TrajetsResponse> getTrajetsFromUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUserId();
        List<Trajet> trajets = trajetService.findByUserId(userId);

        return ResponseEntity.ok(
                TrajetsResponse.builder()
                        .trajets(trajets)
                        .build()
        );
    }
}
