package tn.enova.Services;

import tn.enova.Models.Requests.AuthenticationRequest;
import tn.enova.Models.Responses.AuthenticationResponse;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface AuthenticationService {
    CompletableFuture<?> register(AuthenticationRequest request) throws IOException;
    AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception;
    String getUsername();
}
