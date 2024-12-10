package tn.enova.Exceptions;


import tn.enova.Enums.ReponseStatus;
import tn.enova.Models.Responses.MsgReponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RessourceFoundException.class)
    public ResponseEntity<?> resourceFoundHandling(RessourceFoundException exception, WebRequest request) {
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("Found Exception")
                .status(ReponseStatus.ERROR)
                .datestamp(new Date())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
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

}