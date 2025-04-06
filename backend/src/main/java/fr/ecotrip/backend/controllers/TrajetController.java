package fr.ecotrip.backend.controllers;

import fr.ecotrip.backend.Security.UserPrincipal;
import fr.ecotrip.backend.dto.TrajetRequest;
import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.model.User;
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
@RequestMapping("/trajets")
@RequiredArgsConstructor
public class TrajetController {

    private final TrajetService trajetService;
    private final UserService userService;

    @GetMapping
    public List<Trajet> getAllUsers() {
        return trajetService.findAll();
    }

    @PostMapping("/create")
    public void createTrajet(@RequestBody TrajetRequest trajetDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.getUserId();

        User user = userService.findById(userId);

        Trajet trajet = Trajet
                .builder()
                .depart(trajetDto.getDepart())
                .arrivee(trajetDto.getArrivee())
                .kCo2(trajetDto.getKCo2())
                .moyenTransport(trajetDto.getMoyenTransport())
                .user(user)
                .build();

        trajetService.createTrajet(trajet);
    }
}
