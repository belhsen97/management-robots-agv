package tn.enova.Services.Interfaces;


import tn.enova.Models.Commons.notification.Notification;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Service("notification-service")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final WebClient webClient;

    @Override
    @Async
    public CompletableFuture<?> send(Notification payload) {
        return   webClient.method(HttpMethod.POST)
                .uri("/user/send")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(MsgReponseStatus.class)
                .toFuture();
               // .thenApply(response -> { return response; });
    }
}
