package fr.ecotrip.backend.service;


import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.repository.TrajetRepository;
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

}
