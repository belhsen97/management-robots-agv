package com.enova.web.api.Controllers;

import com.enova.web.api.Enums.ReponseStatus;
import com.enova.web.api.Models.Commons.mail.Msg;
import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Services.SmtpMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/mail")
public class MailController {
    private final SmtpMailService iService;
    @Autowired
    public MailController(@Qualifier("service-mail-smtp") SmtpMailService iService) {
        this.iService = iService;
    }
    @Async("mail-smtp")
    @PostMapping()
    public CompletableFuture<?> sendMail(@RequestPart(name ="files" , required = false) MultipartFile[] files   , @RequestPart(name ="msg" ) Msg msg) throws MessagingException, IOException {
        msg.addAttachments(files);
        this.iService.sendingMessage(msg);
        return CompletableFuture.completedFuture(MsgReponseStatus.builder().title("message").datestamp(new Date()).status(ReponseStatus.SUCCESSFUL).message("success").build());
    }
}
