package fr.ecotrip.backend.controller;

import fr.ecotrip.backend.security.UserPrincipal;
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

@RestController
@RequestMapping("/trajets")
@RequiredArgsConstructor
public class TrajetController {

    private final TrajetService trajetService;
    private final UserService userService;
    private final Co2Service co2Service;

    @GetMapping
    public ResponseEntity<?> getAllTrajets() {
        List<Trajet> trajets = trajetService.findAll();
        return ResponseEntity.ok(trajets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTrajet(@PathVariable Long id) {
        Trajet trajet = trajetService.findOne(id);
        return ResponseEntity.ok(trajet);
    }


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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrajet(@PathVariable Long id) {
        trajetService.deleteById(id);
        return ResponseEntity.ok("Trajet supprimé avec succès.");
    }


    @GetMapping("/{moyenTransport}/{km}")
    public ResponseEntity<KCo2Response> calculCo2(@PathVariable Long moyenTransport, @PathVariable float km) {
        Double kco2 = co2Service.getKco2(moyenTransport, km);

        return ResponseEntity.ok(
                KCo2Response.builder()
                        .kCo2(kco2)
                        .build()
        );
    }

    @PostMapping("/init")
    public void initializeCo2() {

        co2Service.initCo2();

        ResponseEntity.ok("Créer");
    }

    @GetMapping("/{id}/communs")
    public ResponseEntity<?> getTrajetsCommuns(@PathVariable Long id) {
        List<TrajetCommunResponse> trajetsCommuns = trajetService.findTrajetsCommuns(id);
        return ResponseEntity.ok(trajetsCommuns);
    }

    @GetMapping("/communs")
    public ResponseEntity<?> getAllTrajetsCommuns() {
        List<TrajetCommunResponse> trajetsCommuns = trajetService.getAllTrajetsCommuns();
        return ResponseEntity.ok(trajetsCommuns);
    }

}
