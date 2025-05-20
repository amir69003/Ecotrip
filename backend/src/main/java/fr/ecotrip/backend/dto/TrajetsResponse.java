package fr.ecotrip.backend.dto;

import fr.ecotrip.backend.model.Trajet;
import lombok.Getter;
import lombok.Builder;

import java.util.List;

/**
 * DTO eprésentant la réponse pour une liste de trajets.
 * Cette classe encapsule une collection de trajets pour le transfert de données.
 */
@Getter
@Builder
public class TrajetsResponse {

    private final List<Trajet> trajets;

}


