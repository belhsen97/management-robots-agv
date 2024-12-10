package tn.enova.Services.Interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import tn.enova.Models.Commons.mail.Msg;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Service("notification-service")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final WebClient webClient;

    @Override
    public CompletableFuture<?> send(NotificationResponse notify) {
        return   webClient.method(HttpMethod.POST)
                .uri("lb://notification-service/management-robot/notification-service/user/notify")
                .bodyValue(notify)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(String.class).flatMap(body -> {
                            return Mono.error(new WebClientResponseException(
                                    response.statusCode().value(),
                                    response.statusCode().getReasonPhrase(),
                                    response.headers().asHttpHeaders(),
                                    body.getBytes(),
                                    null));
                        }))
                .bodyToMono(MsgReponseStatus.class)
                .toFuture();
               // .thenApply(response -> { return response; });
    }
    @Override
    public CompletableFuture<?> send(Msg msg) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("msg", msg);
        return   webClient.method(HttpMethod.POST)
                .uri("lb://notification-service/management-robot/notification-service/mail")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(String.class).flatMap(body -> {
                            return Mono.error(new WebClientResponseException(
                                    response.statusCode().value(),
                                    response.statusCode().getReasonPhrase(),
                                    response.headers().asHttpHeaders(),
                                    body.getBytes(),
                                    null));
                        }))
                .bodyToMono(MsgReponseStatus.class)
                .toFuture();
    }

}
