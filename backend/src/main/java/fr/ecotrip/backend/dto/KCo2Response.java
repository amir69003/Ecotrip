package fr.ecotrip.backend.dto;

import lombok.Getter;
import lombok.Builder;

/**
 * DTO représentant la réponse pour les émissions de CO2.
 */
@Getter
@Builder
public class KCo2Response {

    private final Double kCo2;

}
