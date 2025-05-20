package fr.ecotrip.backend.repositories;

import fr.ecotrip.backend.model.Co2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository pour la gestion des émissions de CO2.
 * Fournit les méthodes d'accès aux données pour l'entité Co2.
 */
@Repository
public interface Co2Repository extends JpaRepository<Co2, Long> {

}
