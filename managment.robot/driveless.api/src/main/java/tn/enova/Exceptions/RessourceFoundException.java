package tn.enova.Exceptions;

public class RessourceFoundException extends RuntimeException{
    public RessourceFoundException(Throwable cause) {
        super(cause);
    }

    public RessourceFoundException(String message) {
        super(message);
    }

    public RessourceFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
