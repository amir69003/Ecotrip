package fr.ecotrip.backend.exeption;

public class InvalidTransportException extends RuntimeException {
    public InvalidTransportException(String message) {
        super(message);
    }
}
