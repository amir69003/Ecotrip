package fr.ecotrip.backend.dto;

import io.micrometer.common.lang.NonNullFields;
import lombok.Builder;
import lombok.Getter;

/**
 * DTO représentant la requête pour la création ou la modification d'un trajet.
 * Cette classe contient toutes les informations nécessaires pour créer ou modifier un trajet,
 * incluant les coordonnées géographiques de départ et d'arrivée, ainsi que les informations
 * sur le moyen de transport et les émissions de CO2 associées.
 */
@Getter
@Builder
@NonNullFields
public class TrajetRequest {

    private String depart;
    private Double departLatitude;
    private Double departLongitude;
    
    private String arrivee;
    private Double arriveeLatitude;
    private Double arriveeLongitude;
    
    private String moyenTransport;
    private Double kCo2;
}
