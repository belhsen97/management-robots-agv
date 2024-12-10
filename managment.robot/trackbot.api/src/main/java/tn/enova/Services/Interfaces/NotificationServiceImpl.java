package tn.enova.Services.Interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import tn.enova.Enums.LevelType;
import tn.enova.Enums.ReponseStatus;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service("notification-service")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final WebClient webClient;
    private final HttpServletRequest request;
    @Override
    public CompletableFuture<?> notify(NotificationResponse ntf) {
        if (ntf.getFrom() == null) {
            String userId = (String) this.request.getAttribute("auth-user-id");
            if (userId == null) {
                return CompletableFuture.completedFuture(MsgReponseStatus.builder().title("Error Message")
                        .status(ReponseStatus.ERROR).datestamp(new Date()).message("User ID is missing. Notification not sent.").build());
            }
            ntf.setFrom(userId);
        }
        return this.send(ntf);
    }
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

}
