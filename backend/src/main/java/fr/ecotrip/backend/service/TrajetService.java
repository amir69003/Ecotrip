package fr.ecotrip.backend.service;


import fr.ecotrip.backend.dto.TrajetCommunResponse;
import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.repository.TrajetRepository;
import fr.ecotrip.backend.util.DistanceCalculator;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.ecotrip.backend.security.UserPrincipal;
import fr.ecotrip.backend.exeption.ForbiddenActionException;
import fr.ecotrip.backend.exeption.NoTrajetsFoundException;
import fr.ecotrip.backend.exeption.UnauthenticatedUserException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrajetService {

    public final TrajetRepository trajetRepository;


    public List<Trajet> findAll() {
        return trajetRepository.findAll();
    }

    public Trajet findOne(Long id) {
        return trajetRepository.findById(id)
                .orElseThrow(() -> new NoTrajetsFoundException("Trajet avec l'ID " + id + " non trouvé."));
    }
    

    public void createTrajet(Trajet trajet) {

        trajetRepository.save(trajet);
    }

    public List<Trajet> findByUserId(Long id) {
        return trajetRepository.findAllByUserId(id);
    }
    
    public Trajet findByIdTrajet(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthenticatedUserException("Utilisateur non authentifié.");
        }

        return trajetRepository.findById(id)
                .orElseThrow(() -> new NoTrajetsFoundException("Trajet avec l'ID " + id + " non trouvé."));
    }

    public void deleteById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new UnauthenticatedUserException("Utilisateur non authentifié.");
        }

        Trajet trajet = this.findByIdTrajet(id);
        Long userId = principal.getUserId();

        if (!trajet.getUser().getId().equals(userId)) {
            throw new ForbiddenActionException("Vous n'êtes pas autorisé à supprimer ce trajet.");
        }

        trajetRepository.deleteById(id);
    }

    public List<TrajetCommunResponse> findTrajetsCommuns(Long trajetId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthenticatedUserException("Utilisateur non authentifié.");
        }

        Trajet trajetReference = findOne(trajetId);
        double distanceTotale = DistanceCalculator.calculateDistance(
                trajetReference.getDepartLatitude(),
                trajetReference.getDepartLongitude(),
                trajetReference.getArriveeLatitude(),
                trajetReference.getArriveeLongitude()
        );

        double distanceMaxAcceptable = distanceTotale * 0.1;

        List<Trajet> tousLesTrajets = trajetRepository.findAll().stream()
                .filter(t -> !t.getUser().getId().equals(trajetReference.getUser().getId()))
                .toList();

        return tousLesTrajets.stream()
                .map(trajet -> {
                    double distanceDepart = DistanceCalculator.calculateDistance(
                            trajetReference.getDepartLatitude(),
                            trajetReference.getDepartLongitude(),
                            trajet.getDepartLatitude(),
                            trajet.getDepartLongitude()
                    );

                    double distanceArrivee = DistanceCalculator.calculateDistance(
                            trajetReference.getArriveeLatitude(),
                            trajetReference.getArriveeLongitude(),
                            trajet.getArriveeLatitude(),
                            trajet.getArriveeLongitude()
                    );

                    if (distanceDepart <= distanceMaxAcceptable && distanceArrivee <= distanceMaxAcceptable) {
                        return TrajetCommunResponse.builder()
                                .trajet(trajet)
                                .userId(trajet.getUser().getId())
                                .username(trajet.getUser().getUsername())
                                .email(trajet.getUser().getEmail())
                                .distanceDepart(distanceDepart)
                                .distanceArrivee(distanceArrivee)
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public List<TrajetCommunResponse> getAllTrajetsCommuns() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthenticatedUserException("Utilisateur non authentifié.");
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUserId();

        List<Trajet> trajetsUtilisateur = findByUserId(userId);

        List<Trajet> autresTrajets = trajetRepository.findAll().stream()
                .filter(t -> !t.getUser().getId().equals(userId))
                .toList();

        return trajetsUtilisateur.stream()
                .flatMap(trajetReference -> {
                    double distanceTotale = DistanceCalculator.calculateDistance(
                            trajetReference.getDepartLatitude(),
                            trajetReference.getDepartLongitude(),
                            trajetReference.getArriveeLatitude(),
                            trajetReference.getArriveeLongitude()
                    );

                    double distanceMaxAcceptable = distanceTotale * 0.1;

                    return autresTrajets.stream()
                            .map(trajet -> {
                                double distanceDepart = DistanceCalculator.calculateDistance(
                                        trajetReference.getDepartLatitude(),
                                        trajetReference.getDepartLongitude(),
                                        trajet.getDepartLatitude(),
                                        trajet.getDepartLongitude()
                                );

                                double distanceArrivee = DistanceCalculator.calculateDistance(
                                        trajetReference.getArriveeLatitude(),
                                        trajetReference.getArriveeLongitude(),
                                        trajet.getArriveeLatitude(),
                                        trajet.getArriveeLongitude()
                                );

                                if (distanceDepart <= distanceMaxAcceptable && distanceArrivee <= distanceMaxAcceptable) {
                                    return TrajetCommunResponse.builder()
                                            .trajet(trajet)
                                            .userId(trajet.getUser().getId())
                                            .username(trajet.getUser().getUsername())
                                            .email(trajet.getUser().getEmail())
                                            .distanceDepart(distanceDepart)
                                            .distanceArrivee(distanceArrivee)
                                            .build();
                                }
                                return null;
                            })
                            .filter(Objects::nonNull);
                })
                .toList();
    }

}
