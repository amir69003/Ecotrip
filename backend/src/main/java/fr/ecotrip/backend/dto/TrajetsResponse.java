package fr.ecotrip.backend.dto;

import fr.ecotrip.backend.model.Trajet;
import lombok.Getter;
import lombok.Builder;

import java.util.List;

@Getter
@Builder
public class TrajetsResponse {

    private final List<Trajet> trajets;

}


