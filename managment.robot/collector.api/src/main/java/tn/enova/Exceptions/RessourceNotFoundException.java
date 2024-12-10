package tn.enova.Exceptions;


public class RessourceNotFoundException extends RuntimeException{
    public RessourceNotFoundException(Throwable cause) {
        super(cause);
    }

    public RessourceNotFoundException(String message) {
        super(message);
    }

    public RessourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
