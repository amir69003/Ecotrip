package fr.ecotrip.backend.dto;

import fr.ecotrip.backend.model.Trajet;
import lombok.Builder;
import lombok.Data;

/**
 * DTO représentant la réponse pour un trajet commun entre utilisateurs.
 * Cette classe contient les informations sur un trajet partagé, incluant les distances
 * entre les points de départ et d'arrivée des différents utilisateurs.
 */
@Data
@Builder
public class TrajetCommunResponse {
    private Trajet trajet;
    private Long userId;
    private String username;
    private Double distanceDepart; 
    private Double distanceArrivee;
} 