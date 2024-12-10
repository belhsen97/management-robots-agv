package tn.enova.Exceptions;


import org.springframework.stereotype.Component;
import tn.enova.Listener.CustomExceptionHandler;


@Component
public class GlobalExceptionHandler2 {
    @CustomExceptionHandler(RessourceFoundException.class)
    public void handleResourceFoundException(RessourceFoundException e) {
        System.out.println("Haaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + e.getMessage());
    }
}