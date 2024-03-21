package com.enova.web.api.Controllers;

import com.enova.web.api.Dtos.AuthenticationRequestDto;
import com.enova.web.api.Dtos.AuthenticationResponseDto;
import com.enova.web.api.Dtos.MsgReponseStatus;
import com.enova.web.api.Services.IAuthenticationService;
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

    private final IAuthenticationService iService;

    @Autowired
    public AuthenticationController(@Qualifier("authentication-service") IAuthenticationService iService) {
        this.iService = iService;
    }

    @PostMapping("/register")
    public ResponseEntity<MsgReponseStatus> register(@Validated @RequestBody AuthenticationRequestDto request) throws MessagingException, IOException {
        return new ResponseEntity<>(iService.register(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto request) throws Exception {
        return ResponseEntity.ok(iService.authenticate(request));
    }



}
