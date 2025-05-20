package fr.ecotrip.backend.dto;

import io.micrometer.common.lang.NonNullFields;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

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
