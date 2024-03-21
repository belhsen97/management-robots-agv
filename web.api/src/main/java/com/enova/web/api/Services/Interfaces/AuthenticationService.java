package com.enova.web.api.Services.Interfaces;

import com.enova.web.api.Dtos.AuthenticationRequestDto;
import com.enova.web.api.Dtos.AuthenticationResponseDto;
import com.enova.web.api.Dtos.MsgReponseStatus;
import com.enova.web.api.Dtos.ReponseStatus;
import com.enova.web.api.Dtos.mail.BodyContent;
import com.enova.web.api.Dtos.mail.Msg;
import com.enova.web.api.Dtos.mail.TypeBody;
import com.enova.web.api.Enums.Roles;
import com.enova.web.api.Enums.TokenType;
import com.enova.web.api.Entitys.Token;
import com.enova.web.api.Entitys.User;
import com.enova.web.api.Exceptions.MethodArgumentNotValidException;
import com.enova.web.api.Repositorys.UserRepository;
import com.enova.web.api.Securitys.JwtService;
import com.enova.web.api.Services.IAuthenticationService;
import com.enova.web.api.Services.IFileService;
import com.enova.web.api.Services.IsmtpMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@Service("authentication-service")
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IFileService ifileService;
    private final IsmtpMailService ismtpMailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MsgReponseStatus register(AuthenticationRequestDto request) throws IOException, MessagingException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new MethodArgumentNotValidException("other username found");
        }
        User user = User.builder()
                .createdAt(new Date())
                .role(Roles.OPERATOR)
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .build();
        String formHTMLForNewUser = ifileService.Edit_ConfirmMailPage(user.getUsername());
        String formHTMLForAdmin = ifileService.Edit_NewUser(user.getUsername(), user.getEmail());
        final Msg msgForNewUser = Msg.builder().subject("Confirmation Address Mail")
                .email(user.getEmail())
                .text("body")
                .bodyContents(new ArrayList<BodyContent>() {{
                    add(BodyContent.builder().content(formHTMLForNewUser).type(TypeBody.HTML).build());
                }}).build();
//        this.ismtpMailService.connect();
//        this.ismtpMailService.sendingMultiBodyContent(msgForNewUser);
//        for ( User admin : userRepository.findUsersByRole(Roles.ADMIN)){
//            final Msg msgForAdmin = Msg.builder().subject("New User Registration Notification")
//                    .email(admin.getEmail())
//                    .text("body")
//                    .bodyContents(new ArrayList<BodyContent>(){{add(BodyContent.builder().content(formHTMLForAdmin).type(TypeBody.HTML).build());}}).build();
//            this.ismtpMailService.sendingMultiBodyContent(msgForAdmin);
//        }
        userRepository.save(user);
        return MsgReponseStatus.builder()
                .title("Register User")
                .status(ReponseStatus.SUCCESSFUL)
                .datestamp(new Date())
                .message("To complete next step you should verify confirmation mail").build();
    }
















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

    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) { return authentication.getName();}
        return "Unknown";
    }
}













