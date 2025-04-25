package fr.ecotrip.backend.exeption;

public class NoTrajetsFoundException extends RuntimeException {
    public NoTrajetsFoundException(String message) {
        super(message);
    }
}

