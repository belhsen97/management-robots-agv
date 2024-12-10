package tn.enova.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class MethodArgumentNotValidException extends RuntimeException {
    public MethodArgumentNotValidException(Throwable cause) {
        super(cause);
    }

    public MethodArgumentNotValidException(String message) {
        super(message);
    }

    public MethodArgumentNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
