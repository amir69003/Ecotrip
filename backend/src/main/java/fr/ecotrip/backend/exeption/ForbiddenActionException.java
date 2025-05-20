package fr.ecotrip.backend.exeption;

/**
 * Exception levée lorsqu'une action est interdite pour l'utilisateur.
 * Cette exception est utilisée pour signaler qu'un utilisateur n'a pas les permissions
 * nécessaires pour effectuer une action spécifique.
 */
public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String message) {
        super(message);
    }
}