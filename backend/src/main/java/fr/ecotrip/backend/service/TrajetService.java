package fr.ecotrip.backend.service;


import fr.ecotrip.backend.model.Trajet;
import fr.ecotrip.backend.repositories.TrajetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import fr.ecotrip.backend.exeption.NoTrajetsFoundException;


import java.util.List;

@Service
@RequiredArgsConstructor
public class TrajetService {

    public final TrajetRepository trajetRepository;


    public List<Trajet> findAll() {
        return trajetRepository.findAll();
    }

    public Trajet findOne(Long id) {
        return trajetRepository.findById(id)
                .orElseThrow(() -> new NoTrajetsFoundException("Trajet avec l'ID " + id + " non trouvé."));
    }
    

    public void createTrajet(Trajet trajet) {

        trajetRepository.save(trajet);
    }

    public List<Trajet> findByUserId(Long id) {
        List<Trajet> trajets = trajetRepository.findAllByUserId(id);
        if (trajets.isEmpty()) {
            throw new NoTrajetsFoundException("Aucun trajet trouvé pour l'utilisateur avec l'ID " + id);
        }
        return trajets;
    }
    

    public Trajet findByIdTrajet(Long id) {
        return trajetRepository.findById(id)
                .orElseThrow(() -> new NoTrajetsFoundException("Trajet avec l'ID " + id + " non trouvé."));
    }

    public void deleteById(Long id) {
        trajetRepository.deleteById(id);
    }
}
