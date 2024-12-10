package tn.enova.Controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import tn.enova.Models.Commons.mail.Msg;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailNotificatonRestController {

    private final WebClient.Builder webClientBuilder;

    @Async("web-client")
    @PostMapping()
    public CompletableFuture<?> sendMail(@RequestPart(name = "files", required = false) MultipartFile[] files, @RequestPart(name = "msg") Msg msg) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("msg", msg);

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                builder.part("files", new ByteArrayResource(file.getBytes()), MediaType.MULTIPART_FORM_DATA)
                        .header("Content-Disposition", "form-data; name=files; filename=" + file.getOriginalFilename());
            }
        }

        return webClientBuilder.build().method(HttpMethod.POST)
                .uri("lb://mail-service/management-robot/mail-service/mail")
                .contentType(MediaType.MULTIPART_FORM_DATA)
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
                .bodyToMono(Object.class)
                .toFuture();
        }
}
