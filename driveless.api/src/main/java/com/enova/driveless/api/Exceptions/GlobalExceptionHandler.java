package com.enova.driveless.api.Exceptions;


import com.enova.driveless.api.Enums.ReponseStatus;
import com.enova.driveless.api.Models.Responses.MsgReponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RessourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundHandling(RessourceNotFoundException exception, WebRequest request) {
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("Not Found Exception")
                .status(ReponseStatus.ERROR)
                .datestamp(new Date())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<?> resourceNotFoundHandling(JsonProcessingException exception, WebRequest request) {
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("Json Processing Exception")
                .status(ReponseStatus.ERROR)
                .datestamp(new Date())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request) {
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("Exception")
                .status(ReponseStatus.ERROR)
                .datestamp(new Date())
                .message(String.valueOf(exception.getClass()))
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}