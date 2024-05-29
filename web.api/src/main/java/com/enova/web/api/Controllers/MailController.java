package com.enova.web.api.Controllers;

import com.enova.web.api.Models.Commons.mail.Msg;
import com.enova.web.api.Services.SmtpMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mail")
public class MailController {
    private final SmtpMailService iService;
    @Autowired
    public MailController(@Qualifier("service-mail-smtp") SmtpMailService iService) {
        this.iService = iService;
    }
    @GetMapping
    public List<String> GetAllAdressMail() {
        return null;
    }
    @PostMapping()
    public void sendMail(@RequestBody Msg msg) {

    }
}
