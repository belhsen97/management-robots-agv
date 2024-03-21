package com.enova.web.api.Services;

import com.enova.web.api.Dtos.AuthenticationRequestDto;
import com.enova.web.api.Dtos.AuthenticationResponseDto;
import com.enova.web.api.Dtos.MsgReponseStatus;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IAuthenticationService {
    MsgReponseStatus register(AuthenticationRequestDto request) throws IOException, MessagingException;
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request) throws Exception;
    String getUsername();
}
