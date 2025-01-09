package tn.enova.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import tn.enova.Enums.ReponseStatus;
import tn.enova.Models.Commons.Attachment;
import tn.enova.Models.Commons.mail.Msg;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Services.SmtpMailService;
import tn.enova.Services.TaskSchedulingService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final SmtpMailService service;
    private final TaskSchedulingService taskSchedulingService;
    private final WebClient.Builder webClientBuilder;


    @Async("mail-smtp")
    @PostMapping()
    public CompletableFuture<?> sendMail(@RequestPart(name = "files", required = false) MultipartFile[] files, @RequestPart(name = "msg") Msg msg) throws MessagingException, IOException {
        msg.addAttachments(files);
        long dataNowUnix = new Date().getTime();
        if (msg.getTimestamp() <= dataNowUnix) {
           this.service.sendingMessage(msg);
            return CompletableFuture.completedFuture(MsgReponseStatus.builder().title("message").datestamp(new Date()).status(ReponseStatus.SUCCESSFUL).message("success send mail").build());
        }
        if (msg.getTimestamp() > dataNowUnix) {
            taskSchedulingService.addScheduleTask(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    service.sendingMessage(msg);
                }
            }, new Date(msg.getTimestamp()));
        }

        return CompletableFuture.completedFuture(MsgReponseStatus.builder().title("message").datestamp(new Date()).status(ReponseStatus.SUCCESSFUL).message("success send mail").build());
    }

    @Async("web-client")
    @GetMapping("all-address-mail")
    public CompletableFuture<?> getAllAddressMail() {
        return webClientBuilder.build().method(HttpMethod.GET)
                .uri("lb://user-service/management-robot/user-service/user/all-address-mail")
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