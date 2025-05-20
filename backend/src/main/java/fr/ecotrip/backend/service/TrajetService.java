package fr.ecotrip.backend.service;

import fr.ecotrip.backend.dto.TrajetCommunResponse;
import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.repositories.TrajetRepository;
import fr.ecotrip.backend.util.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import fr.ecotrip.backend.Security.UserPrincipal;
import fr.ecotrip.backend.exeption.ForbiddenActionException;
import fr.ecotrip.backend.exeption.NoTrajetsFoundException;
import fr.ecotrip.backend.exeption.UnauthenticatedUserException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des trajets.
 * Fournit des méthodes pour créer, récupérer, supprimer et gérer les trajets.
 */
@Service
@RequiredArgsConstructor
public class TrajetService {

    private final TrajetRepository trajetRepository;

    /**
     * Récupère tous les trajets.
     * @return Liste de tous les trajets
     */
    public List<Trajet> findAll() {
        return trajetRepository.findAll();
    }

    /**
     * Récupère un trajet par son ID.
     * @param id ID du trajet
     * @return Le trajet correspondant
     * @throws NoTrajetsFoundException si le trajet n'est pas trouvé
     */
    public Trajet findOne(Long id) {
        return trajetRepository.findById(id)
                .orElseThrow(() -> new NoTrajetsFoundException("Trajet avec l'ID " + id + " non trouvé."));
    }

    /**
     * Crée un nouveau trajet.
     * @param trajet Le trajet à créer
     */
    public void createTrajet(Trajet trajet) {
        trajetRepository.save(trajet);
    }

    /**
     * Récupère tous les trajets d'un utilisateur.
     * @param id ID de l'utilisateur
     * @return Liste des trajets de l'utilisateur
     * @throws NoTrajetsFoundException si aucun trajet n'est trouvé
     */
    public List<Trajet> findByUserId(Long id) {
        List<Trajet> trajets = trajetRepository.findAllByUserId(id);
        if (trajets.isEmpty()) {
            throw new NoTrajetsFoundException("Aucun trajet trouvé pour l'utilisateur avec l'ID " + id);
        }
        return trajets;
    }

    /**
     * Récupère un trajet par son ID avec vérification de l'authentification.
     * @param id ID du trajet
     * @return Le trajet correspondant
     * @throws UnauthenticatedUserException si l'utilisateur n'est pas authentifié
     * @throws NoTrajetsFoundException si le trajet n'est pas trouvé
     */
    public Trajet findByIdTrajet(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthenticatedUserException("Utilisateur non authentifié.");
        }
        return trajetRepository.findById(id)
                .orElseThrow(() -> new NoTrajetsFoundException("Trajet avec l'ID " + id + " non trouvé."));
    }

    /**
     * Supprime un trajet par son ID.
     * @param id ID du trajet à supprimer
     * @throws UnauthenticatedUserException si l'utilisateur n'est pas authentifié
     * @throws ForbiddenActionException si l'utilisateur n'est pas autorisé à supprimer le trajet
     */
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

    /**
     * Trouve les trajets communs pour un trajet spécifique.
     * @param trajetId ID du trajet de référence
     * @return Liste des trajets communs
     * @throws UnauthenticatedUserException si l'utilisateur n'est pas authentifié
     */
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
                .collect(Collectors.toList());
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

    /**
     * Récupère tous les trajets communs pour l'utilisateur connecté.
     * @return Liste de tous les trajets communs
     * @throws UnauthenticatedUserException si l'utilisateur n'est pas authentifié
     */
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
                .collect(Collectors.toList());
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
