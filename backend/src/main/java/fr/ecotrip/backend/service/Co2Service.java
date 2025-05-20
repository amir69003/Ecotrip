package fr.ecotrip.backend.service;

import fr.ecotrip.backend.exeption.InvalidTransportException;
import fr.ecotrip.backend.model.Co2;
import fr.ecotrip.backend.repositories.Co2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service de gestion des émissions de CO2.
 * Fournit les méthodes pour le calcul et la gestion des émissions de CO2
 * pour différents moyens de transport.
 */
@Service
@RequiredArgsConstructor
public class Co2Service {

    private final Co2Repository co2Repository;

    private Double getKco2_1(Long id) {
        return co2Repository.findById(id)
                .map(Co2::getKco2)
                .orElse(-1.0);
    }

    /**
     * Récupère la quantité de CO2 émise pour un moyen de transport et une distance donnés.
     * @param moyenTransportId ID du moyen de transport
     * @param km Distance en kilomètres
     * @return Quantité de CO2 émise en kg
     */
    public Double getKco2(Long moyenTransportId, float km) {
        Double kco2ParKm = getKco2_1(moyenTransportId);

        if (kco2ParKm == null || kco2ParKm <= -1.) {
                throw new InvalidTransportException("Moyen de transport introuvable ou valeur non valide");
        }

        return km * kco2ParKm;
    }

    /**
     * Initialise les données de CO2 pour les différents moyens de transport.
     * Crée les entrées de base pour le calcul des émissions de CO2.
     */
    public void initCo2() {
        Co2 bus =
                Co2
                        .builder()
                        .kco2(104.*0.001)
                        .transport("bus")
                        .build();

        Co2 trot =
                Co2
                        .builder()
                        .kco2(2.*0.001)
                        .transport("trotinette")
                        .build();

        Co2 marche =
                Co2
                        .builder()
                        .kco2(0.)
                        .transport("marche")
                        .build();

        Co2 train =
                Co2
                        .builder()
                        .kco2(22.9*0.001)
                        .transport("train")
                        .build();

        Co2 voitureThermique =
                Co2
                        .builder()
                        .kco2(192.0*0.001)
                        .transport("voitureThermique")
                        .build();


        Co2 voitureElec =
                Co2
                        .builder()
                        .kco2(19.8*0.001)
                        .transport("voitureElec")
                        .build();

        Co2 avion =
                Co2
                        .builder()
                        .kco2(150.0 * 0.001)
                        .transport("avion")
                        .build();

        Co2 bateau =
                Co2
                        .builder()
                        .kco2(100.0 * 0.001)
                        .transport("bateau")
                        .build();


        co2Repository.save(marche);
        co2Repository.save(trot);
        co2Repository.save(voitureElec);
        co2Repository.save(train);
        co2Repository.save(bus);
        co2Repository.save(voitureThermique);
        co2Repository.save(avion);
        co2Repository.save(bateau);
    }

}
