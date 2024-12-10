package tn.enova.Exceptions;

public class NotificationBadFormat extends NotificationException {

    public NotificationBadFormat(Throwable cause) {
        super(cause);
    }

    public NotificationBadFormat(String message) {
        super(message);
    }

    public NotificationBadFormat(String message, Throwable cause) {
        super(message, cause);
    }
}