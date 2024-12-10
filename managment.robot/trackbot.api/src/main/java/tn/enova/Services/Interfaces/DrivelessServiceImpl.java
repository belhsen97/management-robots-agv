package tn.enova.Services.Interfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import tn.enova.Models.Entitys.Robot;
import tn.enova.Models.Entitys.RobotSetting;
import tn.enova.Models.Entitys.Tag;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Services.DrivelessService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service("driveless-service")
@RequiredArgsConstructor
public class DrivelessServiceImpl implements DrivelessService {
    private final WebClient webClient;
    @Override
    public CompletableFuture<?> insertRobot(Robot robot) {
            return   webClient.method(HttpMethod.POST)
                    .uri("lb://driveless-service/management-robot/driveless-service/robot")
                    .bodyValue(robot)
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

    @Override
    public CompletableFuture<?> updateRobot(String id, Robot robot) {
        return   webClient.method(HttpMethod.PUT)
                .uri("lb://driveless-service/management-robot/driveless-service/robot/"+id)
                .bodyValue(robot)
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

    @Override
    public CompletableFuture<?> deleteRobot(String id) {
        return   webClient.method(HttpMethod.DELETE)
                .uri("lb://driveless-service/management-robot/driveless-service/robot/"+id)
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

    @Override
    public CompletableFuture<?> deleteAllRobots() {
        return   webClient.method(HttpMethod.DELETE)
                .uri("lb://driveless-service/management-robot/driveless-service/robot")
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

    @Override
    public CompletableFuture<?> insertTag(Tag tag) {
        return   webClient.method(HttpMethod.POST)
                .uri("lb://driveless-service/management-robot/driveless-service/tag")
                .bodyValue(tag)
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

    @Override
    public CompletableFuture<?> updateTag(String id, Tag tag) {
        return   webClient.method(HttpMethod.PUT)
                .uri("lb://driveless-service/management-robot/driveless-service/tag/"+id)
                .bodyValue(tag)
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

    @Override
    public CompletableFuture<?> deleteTag(String id) {
        return   webClient.method(HttpMethod.DELETE)
                .uri("lb://driveless-service/management-robot/driveless-service/tag/"+id)
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

    @Override
    public CompletableFuture<?> deleteAllTags() {
        return   webClient.method(HttpMethod.DELETE)
                .uri("lb://driveless-service/management-robot/driveless-service/tag")
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

    @Override
    public CompletableFuture<?> insertRobotSetting(RobotSetting robotSetting) {
        return   webClient.method(HttpMethod.POST)
                .uri("lb://driveless-service/management-robot/driveless-service/robot-setting")
                .bodyValue(robotSetting)
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

    @Override
    public CompletableFuture<?> updateRobotSetting(String id, RobotSetting robotSetting) {
        return   webClient.method(HttpMethod.PUT)
                .uri("lb://driveless-service/management-robot/driveless-service/robot-setting/"+id)
                .bodyValue(robotSetting)
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


    @Override
    public CompletableFuture<?> updateMultiRobotSetting(List<RobotSetting> list) {
        return   webClient.method(HttpMethod.PUT)
                .uri("lb://driveless-service/management-robot/driveless-service/robot-setting")
                .bodyValue(list)
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

    @Override
    public CompletableFuture<?> deleteRobotSetting(String id) {
        return   webClient.method(HttpMethod.DELETE)
                .uri("lb://driveless-service/management-robot/driveless-service/robot-setting/"+id)
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

    @Override
    public CompletableFuture<?> deleteAllRobotSettings() {
        return   webClient.method(HttpMethod.DELETE)
                .uri("lb://driveless-service/management-robot/driveless-service/robot-setting")
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
