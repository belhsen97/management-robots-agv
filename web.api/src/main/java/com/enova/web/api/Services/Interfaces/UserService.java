package com.enova.web.api.Services.Interfaces;

import com.enova.web.api.Dtos.AuthenticationRequestDto;
import com.enova.web.api.Dtos.MsgReponseStatus;
import com.enova.web.api.Dtos.ReponseStatus;
import com.enova.web.api.Dtos.mail.BodyContent;
import com.enova.web.api.Dtos.mail.Msg;
import com.enova.web.api.Dtos.mail.TypeBody;
import com.enova.web.api.Entitys.Attachment;
import com.enova.web.api.Entitys.Enums.Roles;
import com.enova.web.api.Entitys.User;
import com.enova.web.api.Exceptions.MethodArgumentNotValidException;
import com.enova.web.api.Exceptions.RessourceNotFoundException;
import com.enova.web.api.Repositorys.UserRepository;
import com.enova.web.api.Services.IFileService;
import com.enova.web.api.Services.IUserService;
import com.enova.web.api.Services.IsmtpMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service("user-service")
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final IFileService ifileService;
    private final IsmtpMailService ismtpMailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, IFileService ifileService, IsmtpMailService ismtpMailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.ifileService = ifileService;
        this.ismtpMailService = ismtpMailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> selectAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User selectById(String id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public User selectByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User insert(User object) {
        object.setCreatedAt(new Date());
        object.setRole(Roles.OPERATOR);
        object.setEnabled(false);
        return userRepository.save(object);
    }


    @Override
    public User update(String id, User object) {
        User user = this.selectById(id);
        if (user != null) {
            user.setUsername(object.getUsername());
            user.setFirstname(object.getFirstname());
            user.setLastname(object.getLastname());
            user.setMatricule(object.getMatricule());
            user.setPhone(object.getPhone());
            user.setEmail(object.getEmail());
            user.setGender(object.getGender());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        User user = this.selectById(id);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAll() {
        this.userRepository.deleteAll();
    }

    @Override
    @Transactional
    public Attachment savePhotoToUser(MultipartFile file, String username) throws Exception {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new RessourceNotFoundException("Service Attachment ( savePhotoToAccount )  : cannot found account  : " + username));

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence "
                        + fileName);
            }
            Attachment attachment = Attachment.builder()
                    .fileName(fileName)
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .data(file.getBytes())
                    .build();
            user.setPhoto(attachment);
            user.getPhoto().setId(user.getId());
            user = userRepository.save(user);
            return user.getPhoto();
        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }

    @Override
    public Attachment getAttachmentbyId(String id) {
        final User user = this.selectById(id);
        return user.getPhoto();
    }

    @Override
    public Attachment getAttachmentbyUsername(String username) {
        final User user = this.selectByUsername(username);
        return user.getPhoto();
    }


    @Override
    public MsgReponseStatus register(AuthenticationRequestDto request) throws IOException, MessagingException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new MethodArgumentNotValidException("other username found");
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
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
        this.insert(user);
        return MsgReponseStatus.builder()
                .title("Register User")
                .status(ReponseStatus.SUCCESSFUL)
                .datestamp(new Date())
                .message("To complete next step you should verify confirmation mail").build();
    }


    @Override
    public MsgReponseStatus sendMailCodeForgotPassword(String username, String email) throws IOException, MessagingException {
        Optional<User> user = userRepository.findByUsernameAndEmail(username, email);
        if (user.isEmpty()) {
            return MsgReponseStatus.builder()
                    .title("Forgot Password")
                    .status(ReponseStatus.ERROR)
                    .datestamp(new Date())
                    .message("Your mail or username not correct").build();
        }
        final String code = this.getRandomNumber(100, 999) + "-" + this.getRandomNumber(100, 999) + "-" + this.getRandomNumber(100, 999) + "-" + this.getRandomNumber(100, 999);

        user.get().setCode(code);
        userRepository.save(user.get());

        String file = ifileService.Edit_forgotPasswordPage(user.get().getUsername(), code);
        Msg msg = Msg.builder().subject("Forgot Password")
                .email(email)
                .text("body")
                .bodyContents(new ArrayList<BodyContent>() {{
                    add(BodyContent.builder().content(file).type(TypeBody.HTML).build());
                }}).build();

        this.ismtpMailService.connect();
        this.ismtpMailService.sendingMultiBodyContent(msg);

        return MsgReponseStatus.builder()
                .title("Forgot Password")
                .status(ReponseStatus.SUCCESSFUL)
                .datestamp(new Date())
                .message("Your mail is correct so you see your email for complete next step").build();
    }


    @Override
    public MsgReponseStatus updateForgotPassword(String code, String newPassword) {
        Optional<User> user = userRepository.findUserByCode(code);
        if (!user.isEmpty() && !newPassword.isEmpty()) {
            if (user.get().getCode() == "" || user.get().getCode().isEmpty()) {
                return MsgReponseStatus.builder()
                        .title("Update Password")
                        .status(ReponseStatus.UNSUCCESSFUL)
                        .datestamp(new Date())
                        .message("you didn't  sent  Forgot Password in last moment").build();
            }
            user.get().setPassword(passwordEncoder.encode(newPassword));
            user.get().setCode("");
            userRepository.save(user.get());
            return MsgReponseStatus.builder()
                    .title("Update Password")
                    .status(ReponseStatus.SUCCESSFUL)
                    .datestamp(new Date())
                    .message("Your code is correct and we change password").build();
        } else {
            return MsgReponseStatus.builder()
                    .title("Update Password")
                    .status(ReponseStatus.UNSUCCESSFUL)
                    .datestamp(new Date())
                    .message("Your code is not correct or new password is empty").build();
        }
    }

    @Override
    @Transactional
    public MsgReponseStatus updatePassword(String username, String currentPassword, String newPassword) {
        if (newPassword.isEmpty()) {
            return MsgReponseStatus.builder()
                    .status(ReponseStatus.ERROR)
                    .datestamp(new Date())
                    .message("New Password empty").build();
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return MsgReponseStatus.builder()
                    .status(ReponseStatus.UNSUCCESSFUL)
                    .datestamp(new Date())
                    .message("Cannot found username verify you enter correct")
                    .build();
        }


        boolean matches = passwordEncoder.matches(currentPassword, user.get().getPassword());
        if (!matches) {
            return MsgReponseStatus.builder()
                    .status(ReponseStatus.UNSUCCESSFUL)
                    .datestamp(new Date())
                    .message("Verify your current password")
                    .build();
        }

        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user.get());
        return MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .datestamp(new Date())
                .message("Successful update password")
                .build();
    }

    @Override
    @Transactional
    public MsgReponseStatus updateRoleAndActivate(String username, Roles role, boolean enabled) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return MsgReponseStatus.builder()
                    .title("Update Role & Enable Account")
                    .status(ReponseStatus.UNSUCCESSFUL)
                    .datestamp(new Date())
                    .message("Cannot found username verify you enter correct").build();
        }
        user.get().setRole(role);
        user.get().setEnabled(enabled);
        userRepository.save(user.get());
        return MsgReponseStatus.builder()
                .title("Update Role & Enable Account")
                .status(ReponseStatus.SUCCESSFUL)
                .datestamp(new Date())
                .message("Successful update role and state enable user").build();
    }

    @Override
    public MsgReponseStatus updateRole(String username, Roles role) {
        User user = this.selectByUsername(username);
        if (user != null) {
            user.setRole(role);
            userRepository.save(user);
            return MsgReponseStatus.builder()
                    .title("Update Role Account")
                    .status(ReponseStatus.SUCCESSFUL)
                    .datestamp(new Date())
                    .message("Successful update role user to " + role).build();
        }
        return MsgReponseStatus.builder()
                .title("Update Role  Account")
                .status(ReponseStatus.UNSUCCESSFUL)
                .datestamp(new Date())
                .message("Cannot found username " + username + " verify you enter correct").build();
    }

    @Override
    public MsgReponseStatus activateUser(String username, boolean enabled) {
        User user = this.selectByUsername(username);
        if (user != null) {
            user.setEnabled(enabled);
            userRepository.save(user);
            return MsgReponseStatus.builder()
                    .title("Enable Account")
                    .status(ReponseStatus.SUCCESSFUL)
                    .datestamp(new Date())
                    .message("Successful update " + (enabled ? "enable" : "disable") + " user").build();
        }
        return MsgReponseStatus.builder()
                .title("Enable Account")
                .status(ReponseStatus.UNSUCCESSFUL)
                .datestamp(new Date())
                .message("Cannot found username  " + username + "  verify you enter correct").build();
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}