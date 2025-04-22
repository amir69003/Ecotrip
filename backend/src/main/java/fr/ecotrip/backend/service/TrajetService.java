package fr.ecotrip.backend.service;


import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.repositories.TrajetRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.ecotrip.backend.Security.UserPrincipal;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new UnauthenticatedUserException("Utilisateur non authentifié.");
        }

        List<Trajet> trajets = trajetRepository.findAllByUserId(id);
        if (trajets.isEmpty()) {
            throw new NoTrajetsFoundException("Aucun trajet trouvé pour l'utilisateur avec l'ID " + id);
        }
        return trajets;
    }
    

    public Trajet findByIdTrajet(Long id) {
        return trajetRepository.findById(id)
                .orElseThrow(() -> new NoTrajetsFoundException("Trajet avec l'ID " + id + " non trouvé."));
    }

    public void deleteById(Long id) {
        trajetRepository.deleteById(id);
    }
}
