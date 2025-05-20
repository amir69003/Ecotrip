package fr.ecotrip.backend.exeption;

/**
 * Exception levée en cas d'erreur interne du serveur.
 * Cette exception est utilisée pour signaler des erreurs non gérées ou inattendues
 * dans le fonctionnement du serveur.
 */
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
