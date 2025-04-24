package fr.ecotrip.backend.service;


import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.repositories.TrajetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrajetService {

    public final TrajetRepository trajetRepository;


    public List<Trajet> findAll() {
        return trajetRepository.findAll();
    }

    public Trajet findOne(Long id) {
        return trajetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trajet avec l'ID " + id + " non trouvé."));
    }

    public void createTrajet(Trajet trajet) {

        trajetRepository.save(trajet);
    }

    public List<Trajet> findByUserId(Long id) {
        return trajetRepository.findAllByUserId(id);
    }

    public Optional<Trajet> findByIdTrajet(Long id) {
        return trajetRepository.findById(id);
    }

    public void deleteById(Long id) {
        trajetRepository.deleteById(id);
    }
}
