package fr.ecotrip.backend.dto;


import fr.ecotrip.backend.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrajetRequest {
    private String depart;
    private String arrivee;
    private String moyenTransport;
    private Double kCo2;
}
