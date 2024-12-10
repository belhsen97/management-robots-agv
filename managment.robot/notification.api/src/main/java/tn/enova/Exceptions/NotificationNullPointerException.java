package tn.enova.Exceptions;

public class NotificationNullPointerException extends NotificationException {

    public NotificationNullPointerException(Throwable cause) {
        super(cause);
    }

    public NotificationNullPointerException(String message) {
        super(message);
    }

    public NotificationNullPointerException(String message, Throwable cause) {
        super(message, cause);
    }
}