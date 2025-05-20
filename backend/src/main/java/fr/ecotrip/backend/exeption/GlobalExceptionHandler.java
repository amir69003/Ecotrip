package fr.ecotrip.backend.exeption;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Gestionnaire global des exceptions de l'application.
 * Intercepte et gère toutes les exceptions non gérées pour fournir des réponses HTTP appropriées.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Gère les exceptions de type UserNotFoundException.
     * @param ex L'exception à gérer
     * @return Réponse HTTP avec le message d'erreur
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


    /**
     * Gère les exceptions de type InternalServerErrorException.
     * @param ex L'exception à gérer
     * @return Réponse HTTP avec le message d'erreur
     */
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<String> handleInternalError(InternalServerErrorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Une erreur interne est survenue.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("L'ID fourni n'est pas valide. Veuillez fournir un nombre.");
    }

    /**
     * Gère les exceptions de type NoTrajetsFoundException.
     * @param ex L'exception à gérer
     * @return Réponse HTTP avec le message d'erreur
     */
    @ExceptionHandler(NoTrajetsFoundException.class)
    public ResponseEntity<String> handleNoTrajetsFoundException(NoTrajetsFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Gère les exceptions de type InvalidTransportException.
     * @param ex L'exception à gérer
     * @return Réponse HTTP avec le message d'erreur
     */
    @ExceptionHandler(InvalidTransportException.class)
    public ResponseEntity<String> handleInvalidTransportException(InvalidTransportException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    /**
     * Gère les exceptions de type UnauthenticatedUserException.
     * @param ex L'exception à gérer
     * @return Réponse HTTP avec le message d'erreur
     */
    @ExceptionHandler(UnauthenticatedUserException.class)
    public ResponseEntity<String> handleUnauthenticatedUserException(UnauthenticatedUserException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * Gère les exceptions de type ForbiddenActionException.
     * @param ex L'exception à gérer
     * @return Réponse HTTP avec le message d'erreur
     */
    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<String> handleForbiddenAction(ForbiddenActionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect.");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> handleDisabled(DisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Le compte est désactivé.");
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<String> handleLocked(LockedException ex) {
        return ResponseEntity.status(HttpStatus.LOCKED).body("Le compte est verrouillé.");
    }


}
