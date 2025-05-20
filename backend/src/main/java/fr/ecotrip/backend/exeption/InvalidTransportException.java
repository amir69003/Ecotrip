package fr.ecotrip.backend.exeption;

/**
 * Exception levée lorsqu'un moyen de transport invalide est spécifié.
 * Cette exception est utilisée pour signaler qu'un moyen de transport
 * n'existe pas ou n'est pas valide dans le contexte de l'application.
 */
public class InvalidTransportException extends RuntimeException {
    public InvalidTransportException(String message) {
        super(message);
    }
}
