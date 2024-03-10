package com.enova.web.api.Services.Interfaces;

import com.enova.web.api.Dtos.AuthenticationRequestDto;
import com.enova.web.api.Dtos.AuthenticationResponseDto;
import com.enova.web.api.Dtos.ReponseStatus;
import com.enova.web.api.Enums.TokenType;
import com.enova.web.api.Entitys.Token;
import com.enova.web.api.Entitys.User;
import com.enova.web.api.Repositorys.UserRepository;
import com.enova.web.api.Securitys.JwtService;
import com.enova.web.api.Services.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

@Service("authentication-service")
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        final User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new Exception("Error findByEmail"));
        String jwtToken = jwtService.generateToken(user);


        //%%%%%%%%%   revokeAllUserTokens where isRevoked or isExpired  %%%%%%%%%%%%
        Set<Token> validUserTokens = user.getTokens();
        System.out.println(validUserTokens.size());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.removeIf(token -> token.isRevoked() || token.isExpired()
                    // ||!jwtService.isTokenValid(token.getToken(), user)
            );
            user.setTokens(validUserTokens);
        }
        System.out.println(validUserTokens.size());
        //%%%%%%%%%   create new tocken where isRevoked or isExpired  %%%%%%%%%%%%
        Token token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        user.addToken(token);


        userRepository.save(user);


        return AuthenticationResponseDto.childBuilder()
                .token(jwtToken)
                .title("Authentication")
                .datestamp(new Date())
                .status(ReponseStatus.SUCCESSFUL)
                .message("Successful to access account")
                .build();
    }
}













