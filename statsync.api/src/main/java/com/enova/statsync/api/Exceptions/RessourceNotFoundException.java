package com.enova.statsync.api.Exceptions;


public class RessourceNotFoundException extends RuntimeException{
    public RessourceNotFoundException(String message){
        super (message);
    }
}
