package com.enova.web.api.Controllers;



import com.enova.web.api.Services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final WebClient webClient;
    private final AuthenticationService authService;
    @Autowired
    public NotificationController( WebClient webClient, @Qualifier("authentication-service")   AuthenticationService authService ) {
        this.webClient = webClient;
        this.authService = authService;
    }
    @Async
    @GetMapping("{payload}")
    public CompletableFuture<?> GetAll(@PathVariable String payload) {
        String username =  authService.getUsername();

        return   webClient.method(HttpMethod.GET)
                .uri(builder -> builder.queryParam("username", username).build())
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)

                .toFuture()
                .thenApply(response -> {
                    System.out.println("Response from Server B: " + response);
                    return response;
                });
    }





 /*   @Async
    @GetMapping("{payload}")
    public CompletableFuture<?> GetAll(@PathVariable String payload) {
        return   webClient.method(HttpMethod.GET)
                .uri("/controller")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)

                .toFuture()
                .thenApply(response -> {
                    System.out.println("Response from Server B: " + response);
                    return response;
                });

//                .exceptionally(ex -> {
//                    if (ex.getCause() instanceof WebClientRequestException) {
//                        WebClientRequestException wcre = (WebClientRequestException) ex.getCause();
//                        if (wcre.getRootCause() instanceof ConnectException) {
//                            System.err.println("Connection refused to server.");
//                        }
//                    }
//                    return "Failed to fetch notifications: " + ex.getMessage();
//                });
//.subscribe(response -> { System.out.println("Response from Server B: " + response);});
    }*/
}
