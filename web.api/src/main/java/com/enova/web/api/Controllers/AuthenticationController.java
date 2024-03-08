package com.enova.web.api.Controllers;

import com.enova.web.api.Dtos.AuthenticationRequestDto;
import com.enova.web.api.Dtos.AuthenticationResponseDto;
import com.enova.web.api.Services.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

    private IAuthenticationService iauthenticationService;

    @Autowired
    public AuthenticationController(@Qualifier("authentication-service") IAuthenticationService iauthenticationService) {
        this.iauthenticationService = iauthenticationService;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto request) throws Exception {
        return ResponseEntity.ok(iauthenticationService.authenticate(request));
    }
}
