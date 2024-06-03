package com.enova.web.api.Services.Interfaces;


import com.enova.web.api.Enums.*;
import com.enova.web.api.Models.Commons.mail.Msg;
import com.enova.web.api.Models.Commons.mail.Recipient;
import com.enova.web.api.Models.Requests.AuthenticationRequest;
import com.enova.web.api.Models.Responses.AuthenticationResponse;
import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Models.Commons.mail.BodyContent;
import com.enova.web.api.Models.Entitys.Token;
import com.enova.web.api.Models.Entitys.User;
import com.enova.web.api.Exceptions.MethodArgumentNotValidException;
import com.enova.web.api.Repositorys.UserRepository;
import com.enova.web.api.Securitys.JwtService;
import com.enova.web.api.Services.AuthenticationService;
import com.enova.web.api.Services.FileService;
import com.enova.web.api.Services.SmtpMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

@Service("authentication-service")
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final FileService ifileService;
    private final SmtpMailService smtpMailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MsgReponseStatus register(AuthenticationRequest request) throws IOException, MessagingException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new MethodArgumentNotValidException("other username found");
        }
        User user = User.builder()
                .createdAt(new Date())
                .role(Roles.OPERATOR)
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .build();
        final String formHTMLForNewUser = ifileService.Edit_ConfirmMailPage(user.getUsername());
        final String formHTMLForAdmin = ifileService.Edit_NewUser(user.getUsername(), user.getEmail());


        List<Recipient> recipientList =  new ArrayList<Recipient>() {{add(Recipient.builder().address(user.getEmail()).type(RecipientType.TO).build());} };
        List<BodyContent> bodyContentList =  new ArrayList<BodyContent>() {{add(BodyContent.builder().content(formHTMLForNewUser).type(TypeBody.HTML).build());}};
        Msg msg = Msg.builder().subject("Confirmation Address Mail").recipients(recipientList).bodyContents(bodyContentList).build();
        this.smtpMailService.sendingMessage(msg);


        recipientList = new ArrayList<Recipient>();
        List<User> adminList =  userRepository.findUsersByRole(Roles.ADMIN);
        for ( User admin : adminList){
            recipientList.add(Recipient.builder().address(admin.getEmail()).type(RecipientType.TO).build());
        }
        if ( !adminList.isEmpty()) {
            bodyContentList = new ArrayList<BodyContent>() {{add(BodyContent.builder().content(formHTMLForAdmin).type(TypeBody.HTML).build());}};
            msg = Msg.builder().subject("Confirmation Address Mail").recipients(recipientList).bodyContents(bodyContentList).build();
            this.smtpMailService.sendingMessage(msg);
        }




        userRepository.save(user);
        return MsgReponseStatus.builder()
                .title("Register User")
                .status(ReponseStatus.SUCCESSFUL)
                .datestamp(new Date())
                .message("To complete next step you should verify confirmation mail").build();
    }



    @Override
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

        Optional<User> user = this.userRepository.findByUsername(request.getUsername());
        if (user.isEmpty()) {
            throw new Exception("Error found username =" + request.getUsername());
        }
        String jwtToken = jwtService.generateToken(user.get());
        //%%%%%%%%%   revokeAllUserTokens where isRevoked or isExpired  %%%%%%%%%%%%
        Set<Token> validUserTokens = user.get().getTokens();
        System.out.println(validUserTokens.size());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.removeIf(token -> token.isRevoked() || token.isExpired()
                    // ||!jwtService.isTokenValid(token.getToken(), user)
            );
            user.get().setTokens(validUserTokens);
        }
        System.out.println(validUserTokens.size());
        //%%%%%%%%%   create new tocken where isRevoked or isExpired  %%%%%%%%%%%%
        Token token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        user.get().addToken(token);
        userRepository.save(user.get());
        return AuthenticationResponse.childBuilder()
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













