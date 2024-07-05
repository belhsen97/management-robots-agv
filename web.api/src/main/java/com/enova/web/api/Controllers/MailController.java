package com.enova.web.api.Controllers;

import com.enova.web.api.Enums.ReponseStatus;
import com.enova.web.api.Models.Commons.mail.Msg;
import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Services.SmtpMailService;
import com.enova.web.api.Services.TaskSchedulingService;
import com.enova.web.api.Services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/mail")
public class MailController {
    private final SmtpMailService service;
    private final UserService userService;
    private final TaskSchedulingService taskSchedulingService;

    @Autowired
    public MailController(@Qualifier("service-mail-smtp") SmtpMailService service,
                          @Qualifier("user-service") UserService userService,
                          @Qualifier("service-task-scheduling") TaskSchedulingService taskSchedulingService) {
        this.userService = userService;
        this.service = service;
        this.taskSchedulingService = taskSchedulingService;
    }

    @Async("mail-smtp")
    @PostMapping()
    public CompletableFuture<?> sendMail(@RequestPart(name = "files", required = false) MultipartFile[] files, @RequestPart(name = "msg") Msg msg) throws MessagingException, IOException {
        msg.addAttachments(files);
        long dataNowUnix = new Date().getTime();
        if (msg.getTimestamp() <= dataNowUnix) {
            //this.service.sendingMessage(msg);
            return CompletableFuture.completedFuture(MsgReponseStatus.builder().title("message").datestamp(new Date()).status(ReponseStatus.SUCCESSFUL).message("success send mail").build());
        }

        if (msg.getTimestamp() > dataNowUnix) {
            taskSchedulingService.addScheduleTask(new Runnable() {
                @SneakyThrows //??????????????????????????????
                @Override
                public void run() {
                    service.sendingMessage(msg);
                }
            }, new Date(msg.getTimestamp()));
        }
        return CompletableFuture.completedFuture(MsgReponseStatus.builder().title("message").datestamp(new Date()).status(ReponseStatus.SUCCESSFUL).message("success send mail").build());
    }

    @GetMapping("all-address-mail")
    public Set<String> getAllAddressMail() {
        return this.userService.selectAllEmail();
    }
}
