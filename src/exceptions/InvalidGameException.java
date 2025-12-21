package exceptions;

/**
 * Thrown when trying to solve an invalid game
 */
public class InvalidGameException extends Exception {
    public InvalidGameException(String message) {
        super(message);
    }

    public InvalidGameException(String message, Throwable cause) {
        super(message, cause);
    }
}