package fr.ecotrip.backend.exeption;

/**
 * Exception levée lorsqu'un utilisateur non authentifié tente d'accéder à une ressource.
 * Cette exception est utilisée pour signaler qu'un utilisateur doit être authentifié
 * pour accéder à une fonctionnalité ou une ressource spécifique.
 */
public class UnauthenticatedUserException extends RuntimeException {
    public UnauthenticatedUserException(String message) {
        super(message);
    }
}
