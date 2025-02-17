package tn.enova.Services.Interfaces;

import tn.enova.Enums.RecipientType;
import tn.enova.Enums.ReponseStatus;
import tn.enova.Enums.Roles;
import tn.enova.Enums.TypeBody;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Models.Commons.Attachment;
import tn.enova.Models.Commons.mail.BodyContent;
import tn.enova.Models.Commons.mail.Msg;
import tn.enova.Models.Commons.mail.Recipient;
import tn.enova.Models.Entitys.User;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Repositorys.UserRepository;
import tn.enova.Services.FileService;
import tn.enova.Services.NotificationService;
import tn.enova.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;


@Service("user-service")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileService ifileService;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    @Override
    public List<User> selectAll() {
        return this.userRepository.findAll();
    }
    @Override
    public List<User> selectAllByIds(List<String> ids) {
        return (List<User>) this.userRepository.findAllById(ids);
    }
    @Override
    public Set<String> selectAllEmail() {
        Set<String> listEmails = new HashSet<String>();
        List<User> list = this.userRepository.findAll();
        for (User u : list ){   if ( !u.getEmail().isEmpty()){ listEmails.add(u.getEmail());}    }
      return listEmails;
    }

    @Override
    public User selectById(String id) {
        Optional<User> u = this.userRepository.findById(id);  // return this.userRepository.findById(id).orElseThrow();
        if (u.isEmpty()) {
            throw new RessourceNotFoundException("Cannot found user by id =" + id);
        }
        return u.get();
    }

    @Override
    public User selectByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User insert(User object) {
        object.setCreatedAt(new Date());
        object.setRole(Roles.OPERATOR);
        object.setEnabled(true);
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
    public void delete(String id) {
        User user = this.selectById(id);
        userRepository.delete(user);
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
    public CompletableFuture<?> sendMailCodeForgotPassword(String username, String email) throws IOException{
        Optional<User> user = userRepository.findByUsernameAndEmail(username, email);
        if (user.isEmpty()) {
            return CompletableFuture.completedFuture( MsgReponseStatus.builder()
                    .title("Forgot Password")
                    .status(ReponseStatus.ERROR)
                    .datestamp(new Date())
                    .message("Your mail or username not correct").build());
        }
        final String code = this.getRandomNumber(100, 999) + "-" + this.getRandomNumber(100, 999) + "-" + this.getRandomNumber(100, 999) + "-" + this.getRandomNumber(100, 999);

        user.get().setCode(code);
        userRepository.save(user.get());

        String file = ifileService.Edit_forgotPasswordPage(user.get().getUsername(), code);

        final List<Recipient> recipientList =  new ArrayList<Recipient>() {{add(Recipient.builder().address(email).type(RecipientType.TO).build());} };
        final List<BodyContent> bodyContentList =  new ArrayList<BodyContent>() {{add(BodyContent.builder().content(file).type(TypeBody.HTML).build());}};
        Msg msg = Msg.builder().subject("Forgot Password").recipients(recipientList).bodyContents(bodyContentList).build();

        return this.notificationService.send(msg).thenApply(result -> {
                    return CompletableFuture.completedFuture(
                            MsgReponseStatus.builder()
                                    .title("Forgot Password")
                                    .status(ReponseStatus.SUCCESSFUL)
                                    .datestamp(new Date())
                                    .message("Your mail is correct so you see your email for complete next step").build()
                    );
                }
        );
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