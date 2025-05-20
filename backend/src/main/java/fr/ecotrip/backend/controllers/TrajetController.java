package fr.ecotrip.backend.controllers;

import fr.ecotrip.backend.Security.UserPrincipal;
import fr.ecotrip.backend.dto.KCo2Response;
import fr.ecotrip.backend.dto.TrajetCommunResponse;
import fr.ecotrip.backend.dto.TrajetRequest;
import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.model.User;
import fr.ecotrip.backend.service.Co2Service;
import fr.ecotrip.backend.service.TrajetService;
import fr.ecotrip.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des trajets.
 * Routes pour créer, récupérer, supprimer et gérer les trajets.
 */
@RestController
@RequestMapping("/trajets")
@RequiredArgsConstructor
public class TrajetController {

    private final TrajetService trajetService;
    private final UserService userService;
    private final Co2Service co2Service;

    /**
     * Récupère tous les trajets.
     * @return Liste de tous les trajets
     */
    @GetMapping
    public ResponseEntity<?> getAllTrajets() {
        List<Trajet> trajets = trajetService.findAll();
        return ResponseEntity.ok(trajets);
    }

    /**
     * Récupère un trajet spécifique par son ID.
     * @param id ID du trajet à récupérer
     * @return Le trajet correspondant à l'ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTrajet(@PathVariable Long id) {
        Trajet trajet = trajetService.findOne(id);
        return ResponseEntity.ok(trajet);
    }

    /**
     * Crée un nouveau trajet.
     * @param trajetDto Données du trajet à créer
     * @return Message de confirmation
     */
    @PostMapping
    public ResponseEntity<?> createTrajet(@RequestBody @Validated TrajetRequest trajetDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUserId();
        User user = userService.findById(userId);

        Trajet trajet = Trajet.builder()
                .depart(trajetDto.getDepart())
                .departLatitude(trajetDto.getDepartLatitude())
                .departLongitude(trajetDto.getDepartLongitude())
                .arrivee(trajetDto.getArrivee())
                .arriveeLatitude(trajetDto.getArriveeLatitude())
                .arriveeLongitude(trajetDto.getArriveeLongitude())
                .kCo2(trajetDto.getKCo2())
                .moyenTransport(trajetDto.getMoyenTransport())
                .user(user)
                .build();

        trajetService.createTrajet(trajet);
        return ResponseEntity.status(HttpStatus.CREATED).body("Trajet créé avec succès.");
    }

    /**
     * Supprime un trajet par son ID.
     * @param id ID du trajet à supprimer
     * @return Message de confirmation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrajet(@PathVariable Long id) {
        trajetService.deleteById(id);
        return ResponseEntity.ok("Trajet supprimé avec succès.");
    }

    /**
     * Calcule le CO2 pour un trajet donné.
     * @param moyenTransport ID du moyen de transport
     * @param km Distance en kilomètres
     * @return Quantité de CO2 émise
     */
    @GetMapping("/{moyenTransport}/{km}")
    public ResponseEntity<KCo2Response> calculCo2(@PathVariable Long moyenTransport, @PathVariable float km) {
        Double kco2 = co2Service.getKco2(moyenTransport, km);

        return ResponseEntity.ok(
                KCo2Response.builder()
                        .kCo2(kco2)
                        .build()
        );
    }

    /**
     * Initialise les données de CO2.
     */
    @PostMapping("/init")
    public void initializeCo2() {
        co2Service.initCo2();
        ResponseEntity.ok("Créer");
    }

    /**
     * Récupère les trajets communs pour un trajet spécifique.
     * @param id ID du trajet de référence
     * @return Liste des trajets communs
     */
    @GetMapping("/{id}/communs")
    public ResponseEntity<?> getTrajetsCommuns(@PathVariable Long id) {
        List<TrajetCommunResponse> trajetsCommuns = trajetService.findTrajetsCommuns(id);
        return ResponseEntity.ok(trajetsCommuns);
    }

    /**
     * Récupère tous les trajets communs.
     * @return Liste de tous les trajets communs
     */
    @GetMapping("/communs")
    public ResponseEntity<?> getAllTrajetsCommuns() {
        List<TrajetCommunResponse> trajetsCommuns = trajetService.getAllTrajetsCommuns();
        return ResponseEntity.ok(trajetsCommuns);
    }
}
