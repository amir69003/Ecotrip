package fr.ecotrip.backend.repositories;

import fr.ecotrip.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour la gestion des utilisateurs.
 * Fournit les méthodes d'accès aux données pour l'entité User.

 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Recherche un utilisateur par son adresse email.
     * @param email L'adresse email de l'utilisateur à rechercher
     * @return L'utilisateur correspondant à l'email, ou null si aucun utilisateur n'est trouvé
     */
    User findByEmail(String email);

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     * @param username Le nom d'utilisateur à rechercher
     * @return L'utilisateur correspondant au nom d'utilisateur, ou null si aucun utilisateur n'est trouvé
     */
    User findByUsername(String username);
}


