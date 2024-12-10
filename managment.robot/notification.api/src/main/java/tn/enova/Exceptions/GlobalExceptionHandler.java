package tn.enova.Exceptions;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tn.enova.Enums.ReponseStatus;
import tn.enova.Models.Responses.MsgReponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
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
                    .message(ex.getMessage())
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
                            .message("Unable to connect to other server. Please try again later.")
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





    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<?> notificationExceptionHandling(NotificationException exception, WebRequest request) {
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("Not Found Exception")
                .status(ReponseStatus.ERROR)
                .datestamp(new Date())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

   @ExceptionHandler(NotificationNotFound.class)
    public ResponseEntity<?> resourceNotFoundHandling(NotificationNotFound exception, WebRequest request) {
        MsgReponseStatus errorDetails = MsgReponseStatus.builder()
                .title("Not Found Exception")
                .status(ReponseStatus.ERROR)
                .datestamp(new Date())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
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