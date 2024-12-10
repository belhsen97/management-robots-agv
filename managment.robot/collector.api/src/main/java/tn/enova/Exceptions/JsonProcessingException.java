package tn.enova.Exceptions;



public class JsonProcessingException  extends RuntimeException{
    public JsonProcessingException(Throwable cause) {
        super(cause);
    }

    public JsonProcessingException(String message) {
        super(message);
    }

    public JsonProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
