package tn.enova.Services.Interfaces;



import tn.enova.Enums.*;
import tn.enova.Exceptions.MethodArgumentNotValidException;
import tn.enova.Models.Commons.mail.BodyContent;
import tn.enova.Models.Commons.mail.Msg;
import tn.enova.Models.Commons.mail.Recipient;
import tn.enova.Models.Entitys.Token;
import tn.enova.Models.Entitys.User;
import tn.enova.Models.Requests.AuthenticationRequest;
import tn.enova.Models.Responses.AuthenticationResponse;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Repositorys.UserRepository;
import tn.enova.Securitys.JwtService;
import tn.enova.Services.AuthenticationService;
import tn.enova.Services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.enova.Services.NotificationService;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service("authentication-service")
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final FileService ifileService;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CompletableFuture<?> register(AuthenticationRequest request) throws IOException {
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

        this.userRepository.save(user);

        MsgReponseStatus msgReponseStatus = MsgReponseStatus.builder()
                .title("Register User")
                .status(ReponseStatus.SUCCESSFUL)
                .datestamp(new Date())
                .message("To complete the next step, you should verify the confirmation mail.")
                .build();
        return this.notificationService.send(msg).thenCompose(result -> {
                    List<User> adminList = this.userRepository.findUsersByRole(Roles.ADMIN);
                    if (!adminList.isEmpty()) {
                        recipientList.clear();
                        List<String> listAdmin = new ArrayList<>();
                        for (User admin : adminList) {
                            listAdmin.add(admin.getId());
                            recipientList.add(Recipient.builder().address(admin.getEmail()).type(RecipientType.TO).build());
                        }
                        List<BodyContent> adminBodyContentList = new ArrayList<BodyContent>() {{
                            add(BodyContent.builder().content(formHTMLForAdmin).type(TypeBody.HTML).build());
                        }};
                        NotificationResponse notificationAdmin = NotificationResponse.builder().from(request.getUsername()).level(LevelType.INFO).message("new user registry with username : "+request.getUsername()).build();
                        Msg adminMsg = Msg.builder().subject("New User Registration").recipients(recipientList).bodyContents(adminBodyContentList).build();
                        CompletableFuture<?> adminMsgFuture = this.notificationService.send(adminMsg);
                        CompletableFuture<?> notificationAdminFuture = this.notificationService.send(notificationAdmin);
                        return CompletableFuture.allOf(adminMsgFuture, notificationAdminFuture).thenApply(v -> msgReponseStatus);
                    }
                    return CompletableFuture.completedFuture(
                            msgReponseStatus
                    );
                }
        );


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













