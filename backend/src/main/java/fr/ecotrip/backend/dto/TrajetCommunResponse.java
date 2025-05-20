package fr.ecotrip.backend.dto;

import fr.ecotrip.backend.model.Trajet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrajetCommunResponse {
    private Trajet trajet;
    private Long userId;
    private String username;
    private String email;
    private Double distanceDepart;
    private Double distanceArrivee;
} 