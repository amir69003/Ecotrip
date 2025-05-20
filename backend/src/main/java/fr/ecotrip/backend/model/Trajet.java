package fr.ecotrip.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité représentant un trajet dans le système.
 * Cette classe contient les informations d'un trajet, incluant les points de départ et d'arrivée,
 * leurs coordonnées géographiques, le moyen de transport utilisé et les émissions de CO2 associées.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String depart;
    private Double departLatitude;
    private Double departLongitude;
    
    private String arrivee;
    private Double arriveeLatitude;
    private Double arriveeLongitude;
    
    private String moyenTransport;
    private Double kCo2;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
