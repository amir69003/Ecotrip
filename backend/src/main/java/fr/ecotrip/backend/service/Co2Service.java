package fr.ecotrip.backend.service;

import fr.ecotrip.backend.exeption.InvalidTransportException;
import fr.ecotrip.backend.model.Co2;
import fr.ecotrip.backend.repository.Co2Repository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class Co2Service {

    private final Co2Repository co2Repository;
    private final EntityManager entityManager;

    public Double getKco2(Long id, float km) {
        Double kco2ParKm = co2Repository.findById(id)
                .map(Co2::getKco2)
                .orElse(-1.0);

        if (kco2ParKm <= -1) {
            throw new InvalidTransportException("Moyen de transport introuvable ou valeur non valide");
        }

        return km * kco2ParKm;
    }


    @Transactional
    public void initCo2() {
        co2Repository.deleteAll();
        entityManager.createNativeQuery("ALTER SEQUENCE co2_id_seq RESTART WITH 1;").executeUpdate();

        Co2 velo = Co2.builder()
                .kco2(11 * 0.001)
                .transport("Vélo (électrique)")
                .build();

        Co2 trottinette = Co2.builder()
                .kco2(24.9 * 0.001)
                .transport("Trottinette électrique")
                .build();

        Co2 train = Co2.builder()
                .kco2(27.7 * 0.001)
                .transport("Train")
                .build();

        Co2 voitureElec = Co2.builder()
                .kco2(103 * 0.001)
                .transport("Voiture électrique")
                .build();

        Co2 bus = Co2.builder()
                .kco2(113 * 0.001)
                .transport("Bus")
                .build();

        Co2 motoTherm = Co2.builder()
                .kco2(191 * 0.001)
                .transport("Moto thermique")
                .build();

        Co2 voitureThermique = Co2.builder()
                .kco2(218 * 0.001)
                .transport("Voiture thermique")
                .build();

        Co2 avion = Co2.builder()
                .kco2(259 * 0.001)
                .transport("Avion")
                .build();

        Co2 bateau = Co2.builder()
                .kco2(425 * 0.001)
                .transport("Bateau (type croisière)")
                .build();

        co2Repository.save(velo);
        co2Repository.save(trottinette);
        co2Repository.save(train);
        co2Repository.save(voitureElec);
        co2Repository.save(bus);
        co2Repository.save(motoTherm);
        co2Repository.save(voitureThermique);
        co2Repository.save(avion);
        co2Repository.save(bateau);
    }

}
