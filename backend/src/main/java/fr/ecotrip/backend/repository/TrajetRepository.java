package fr.ecotrip.backend.repository;

import fr.ecotrip.backend.model.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    List<Trajet> findAllByUserId(Long userId);
}
