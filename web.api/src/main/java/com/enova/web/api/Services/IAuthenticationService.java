package com.enova.web.api.Services;

import com.enova.web.api.Dtos.AuthenticationRequestDto;
import com.enova.web.api.Dtos.AuthenticationResponseDto;

public interface IAuthenticationService {
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request) throws Exception;
    String getUsername();
}
