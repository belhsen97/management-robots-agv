package tn.enova.Exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tn.enova.Enums.ReponseStatus;
import tn.enova.Models.Responses.MsgReponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import tn.enova.Services.ObjectMapperService;

import java.net.ConnectException;
import java.util.Date;


@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapperService objMapperService;

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<?> handleWebClientResponseException(WebClientResponseException ex)   {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(MsgReponseStatus.builder().title("WebClient Response Exception").status(ReponseStatus.ERROR).datestamp(new Date()).message(ex.getMessage()) .build());
        try {
            MsgReponseStatus errorDetails = objMapperService.fromJson(ex.getResponseBodyAsString(), MsgReponseStatus.class);
            return ResponseEntity.status(ex.getStatusCode())
                    .body(errorDetails);
        } catch (JsonProcessingException e) {
            MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                    .title(ex.getStatusText())
                    .status(ReponseStatus.ERROR)
                    .datestamp(new Date())
                    .message("Failed to parse error response from other service")
                    .build();
            return ResponseEntity.status(ex.getStatusCode())
                    .body(errorDetails);
        }
    }


    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<?> handleWebClientRequestException(WebClientRequestException ex) {
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof ConnectException) {
            // Handle connection refused exception
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body( MsgReponseStatus.builder()
                            .title("WebClient Request Exception")
                            .status(ReponseStatus.ERROR)
                            .datestamp(new Date())
                            .message("Unable to connect to the server. Please try again later.")
                            .build());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(MsgReponseStatus.builder()
                        .title("WebClient Request Exception")
                        .status(ReponseStatus.ERROR)
                        .datestamp(new Date())
                        .message(ex.getMessage())
                        .build());
    }


    // handling specific exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("Validation Exception")
                .status(ReponseStatus.UNSUCCESSFUL)
                .datestamp(new Date())
                .message(ex.getMessage())
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
                .title("Validation Exception")
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
                .title("Not Found Exception")
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
                .title("Accessing Information Files And Directories Exception")
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