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
        List<Trajet> trajets = trajetRepository.findAllByUserId(id);
        if (trajets.isEmpty()) {
            throw new NoTrajetsFoundException("Aucun trajet trouvé pour l'utilisateur avec l'ID " + id);
        }
        return trajets;
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

        // Récupérer le trajet de référence
        Trajet trajetReference = findOne(trajetId);
        double distanceTotale = DistanceCalculator.calculateDistance(
                trajetReference.getDepartLatitude(),
                trajetReference.getDepartLongitude(),
                trajetReference.getArriveeLatitude(),
                trajetReference.getArriveeLongitude()
        );

        // Calculer la distance maximale acceptable (10% de la distance totale)
        double distanceMaxAcceptable = distanceTotale * 0.1;

        // Récupérer tous les trajets sauf ceux de l'utilisateur actuel
        List<Trajet> tousLesTrajets = trajetRepository.findAll().stream()
                .filter(t -> !t.getUser().getId().equals(trajetReference.getUser().getId()))
                .collect(Collectors.toList());

        // Filtrer les trajets qui sont suffisamment proches
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
                                .distanceDepart(distanceDepart)
                                .distanceArrivee(distanceArrivee)
                                .build();
                    }
                    return null;
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
    }

    public List<TrajetCommunResponse> getAllTrajetsCommuns() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthenticatedUserException("Utilisateur non authentifié.");
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUserId();

        // Récupérer tous les trajets de l'utilisateur connecté
        List<Trajet> trajetsUtilisateur = findByUserId(userId);

        // Récupérer tous les autres trajets
        List<Trajet> autresTrajets = trajetRepository.findAll().stream()
                .filter(t -> !t.getUser().getId().equals(userId))
                .collect(Collectors.toList());

        // Pour chaque trajet de l'utilisateur, trouver les trajets communs
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
                                            .distanceDepart(distanceDepart)
                                            .distanceArrivee(distanceArrivee)
                                            .build();
                                }
                                return null;
                            })
                            .filter(response -> response != null);
                })
                .collect(Collectors.toList());
    }

}
