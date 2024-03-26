package com.enova.web.api.Services;

import com.enova.web.api.Models.Requests.AuthenticationRequest;
import com.enova.web.api.Models.Responses.AuthenticationResponse;
import com.enova.web.api.Models.Responses.MsgReponseStatus;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IAuthenticationService {
    MsgReponseStatus register(AuthenticationRequest request) throws IOException, MessagingException;
    AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception;
    String getUsername();
}
