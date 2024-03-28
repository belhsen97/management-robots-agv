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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import java.io.IOException;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService iService;

    @Autowired
    public AuthenticationController(@Qualifier("authentication-service") AuthenticationService iService) {
        this.iService = iService;
    }

    @PostMapping("/register")
    public ResponseEntity<MsgReponseStatus> register(@Validated @RequestBody AuthenticationRequest request) throws MessagingException, IOException {
        return new ResponseEntity<>(iService.register(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws Exception {
        return ResponseEntity.ok(iService.authenticate(request));
    }



}
