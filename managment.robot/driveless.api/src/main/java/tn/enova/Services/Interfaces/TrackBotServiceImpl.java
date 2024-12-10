package tn.enova.Services.Interfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import tn.enova.Models.Commons.Robot;
import tn.enova.Models.Commons.RobotSetting;
import tn.enova.Models.Commons.RobotSettingDto;
import tn.enova.Models.Commons.Tag;
import tn.enova.Services.TrackBotService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service("trackbot-service")
@RequiredArgsConstructor
public class TrackBotServiceImpl implements TrackBotService {
    private final WebClient webClient;
    @Override
    public CompletableFuture<?> getAllRobots() {
        return   webClient.method(HttpMethod.GET)
                .uri("lb://trackbot-service/management-robot/trackbot-service/robot")
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
                .bodyToMono(new ParameterizedTypeReference<List<Robot>>() {})
                .toFuture();
        // .thenApply(response -> { return response; });
        //new ParameterizedTypeReference<List<Robot>>() {} solution for Failed to fetch robots: java.lang.ClassCastException: class java.util.LinkedHashMap cannot be cast to class tn.enova.Models.Entitys.Robot (java.util.LinkedHashMap is in module java.base of loader 'bootstrap'; tn.enova.Models.Entitys.Robot is in unnamed module of loader 'app')
    }

    @Override
    public CompletableFuture<?> getAllTags() {
        return   webClient.method(HttpMethod.GET)
                .uri("lb://trackbot-service/management-robot/trackbot-service/tag")
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
                .bodyToMono(new ParameterizedTypeReference<List<Tag>>() {})
                .toFuture();
    }
    @Override
    public CompletableFuture<?> getAllRobotSettings() {
        return   webClient.method(HttpMethod.GET)
                .uri("lb://trackbot-service/management-robot/trackbot-service/robot-setting/list")
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
                .bodyToMono(new ParameterizedTypeReference<List<RobotSetting>>() {})
                .toFuture();
    }
}
