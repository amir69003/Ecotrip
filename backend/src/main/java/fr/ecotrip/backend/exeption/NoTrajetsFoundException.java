package fr.ecotrip.backend.exeption;

/**
 * Exception levée lorsqu'aucun trajet n'est trouvé.
 * Cette exception est utilisée pour signaler qu'aucun trajet ne correspond
 * aux critères de recherche spécifiés.
 */
public class NoTrajetsFoundException extends RuntimeException {
    public NoTrajetsFoundException(String message) {
        super(message);
    }
}

