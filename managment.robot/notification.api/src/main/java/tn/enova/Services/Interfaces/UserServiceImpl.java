package tn.enova.Services.Interfaces;


import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import tn.enova.Models.Requests.UserRequest;
import tn.enova.Services.UserService;


import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service("user-service")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final WebClient webClient;

    @Override
    public CompletableFuture<?> getAllUsersbyIds(List<String> ids) {
        String idsPath = String.join(",", ids);
        return   webClient.method(HttpMethod.GET)
                .uri("lb://user-service/management-robot/user-service/user/ids/"+idsPath)
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
                .bodyToMono(new ParameterizedTypeReference<List<UserRequest>>() {})
                .toFuture();
       }
    @Override
    public CompletableFuture<UserRequest> getUserByUsername(String username) {
        return   webClient.method(HttpMethod.GET)
                  //  http://localhost:8089/management-robot/user-service/user/username/belhsen97
                .uri("lb://user-service/management-robot/user-service/user/username/"+username)
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
                .bodyToMono(UserRequest.class)
                .toFuture();
       }

}
