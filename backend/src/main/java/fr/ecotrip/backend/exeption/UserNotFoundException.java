package fr.ecotrip.backend.exeption;

/**
 * Exception levée lorsqu'un utilisateur n'est pas trouvé.
 * Cette exception est utilisée pour signaler qu'un utilisateur spécifié
 * n'existe pas dans le système.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
