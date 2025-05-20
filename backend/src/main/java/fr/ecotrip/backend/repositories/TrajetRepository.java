package fr.ecotrip.backend.repositories;

import fr.ecotrip.backend.model.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * Repository pour la gestion des trajets.
 * Fournit les méthodes d'accès aux données pour l'entité Trajet.
 */
@Repository
public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    /**
     * Récupère tous les trajets d'un utilisateur spécifique.
     * @param userId ID de l'utilisateur
     * @return Liste des trajets de l'utilisateur
     */
    List<Trajet> findAllByUserId(Long userId);
}
