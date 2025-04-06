package fr.ecotrip.backend.controllers;

import fr.ecotrip.backend.Security.UserPrincipal;
import fr.ecotrip.backend.dto.TrajetsResponse;
import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.dto.UserResponse;
import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.service.TrajetService;
import fr.ecotrip.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TrajetService trajetService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/create")
    public void createUser(@RequestBody UserRequest user) {
        userService.createUser(user);
    }

    @GetMapping("/trajets")
    public TrajetsResponse getTrajetsFromUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.getUserId();

        // Récupère les trajets depuis la base
        List<Trajet> trajets = trajetService.findByUserId(userId);

        return TrajetsResponse
                .builder()
                .trajets(trajets)
                .build();
    }

}
