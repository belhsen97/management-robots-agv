package com.enova.web.api.Exceptions;

import com.enova.web.api.Dtos.MsgReponseStatus;
import com.enova.web.api.Dtos.ReponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.NoSuchElementException;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(org.springframework.security.authentication.DisabledException.class)
    public ResponseEntity<?> handleAccessDeniedException(org.springframework.security.authentication.DisabledException ex, WebRequest request) {
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("security authentication Disabled")
                .status(ReponseStatus.ERROR)
                .datestamp(new Date())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    // handling specific exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("Validation")
                .status(ReponseStatus.UNSUCCESSFUL)
                .datestamp(new Date())
                .message(ex.toString())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_ACCEPTABLE);
    }

    //https://salithachathuranga94.medium.com/validation-and-exception-handling-in-spring-boot-51597b580ffd
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(org.springframework.web.bind.MethodArgumentNotValidException ex, WebRequest request) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errorMessage.append(error.getDefaultMessage()).append("; ");
        });
        //return ResponseEntity.badRequest().body(errorMessage.toString());
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("Validation")
                .status(ReponseStatus.UNSUCCESSFUL)
                .datestamp(new Date())
                .message(errorMessage.toString())
                //.details(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_ACCEPTABLE);
    }

    // handling specific exception
    @ExceptionHandler(RessourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundHandling(RessourceNotFoundException exception, WebRequest request) {
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("Not Found")
                .status(ReponseStatus.ERROR)
                .datestamp(new Date())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<?> handleAccessDeniedException(NoSuchElementException ex, WebRequest request) {
//        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
//                .title("No Such Element Exception")
//                .status(ReponseStatus.UNSUCCESSFUL)
//                .datestamp(new Date())
//                .message(ex.getMessage())
//                .build();
//        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
//    }

    //an I/O error occurs accessing information using streams, files and directories.
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ExceptionAccessingInformationFilesAndDirectories(IOException exception, WebRequest request) {
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("Accessing Information Files And Directories")
                .status(ReponseStatus.ERROR)
                .datestamp(new Date())
                .message(exception.getMessage())
                //.details(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // handling global exception

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request) {
//        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
//                .title("Exception")
//                .status(ReponseStatus.ERROR)
//                .datestamp(new Date())
//                .message(String.valueOf(exception.getClass()))
//                //.details(request.getDescription(false))
//                .build();
//        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}