package fr.ecotrip.backend.service;


import fr.ecotrip.backend.dto.TrajetRequest;
import fr.ecotrip.backend.dto.TrajetsResponse;
import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.repositories.TrajetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrajetService {

    public final TrajetRepository trajetRepository;


    public List<Trajet> findAll() {
        return trajetRepository.findAll();
    }

    public void createTrajet(Trajet trajet) {

        trajetRepository.save(trajet);
    }

    public List<Trajet> findByUserId(Long id) {
        return trajetRepository.findAllByUserId(id);
    }
}
