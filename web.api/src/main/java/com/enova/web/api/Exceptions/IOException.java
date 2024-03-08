package com.enova.web.api.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.INTERNAL_SERVER_ERROR )
public class IOException extends RuntimeException{
    public IOException(String message){
        super (message);
    }
}
