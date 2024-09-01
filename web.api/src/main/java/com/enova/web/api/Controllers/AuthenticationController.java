package com.enova.web.api.Controllers;

import com.enova.web.api.Models.Requests.AuthenticationRequest;
import com.enova.web.api.Models.Responses.AuthenticationResponse;
import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.Serializable;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    @Autowired
    public AuthenticationController(@Qualifier("authentication-service") AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<MsgReponseStatus> register(@Validated @RequestBody AuthenticationRequest request) throws MessagingException, IOException {
        return new ResponseEntity<>(service.register(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws Exception {
        return ResponseEntity.ok(service.authenticate(request));
    }
}