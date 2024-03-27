package com.enova.collector.api.Exceptions;


public class RessourceNotFoundException extends RuntimeException{
    public RessourceNotFoundException(String message){
        super (message);
    }
}
