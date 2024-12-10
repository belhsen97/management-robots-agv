package tn.enova.Controllers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import tn.enova.Models.Requests.AuthenticationRequest;
import tn.enova.Models.Responses.AuthenticationResponse;
import tn.enova.Services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    @Autowired
    public AuthenticationController(@Qualifier("authentication-service") AuthenticationService service) {
        this.service = service;
    }
    @Async("web-client")
    @PostMapping("/register")
    public CompletableFuture<?> register(@Validated @RequestBody AuthenticationRequest request) throws IOException {
        return  service.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws Exception {
        return ResponseEntity.ok(service.authenticate(request));
    }
    @GetMapping("/authenticator")
    public ResponseEntity<String> authenticator() {
        return ResponseEntity.ok(service.getUsername());
    }
}