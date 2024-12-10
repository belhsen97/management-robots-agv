package tn.enova.Controllers;


import org.springframework.scheduling.annotation.Async;
import tn.enova.Enums.ReponseStatus;
import tn.enova.Enums.Roles;
import tn.enova.Mappers.AttachmentMapper;
import tn.enova.Mappers.UserMapper;
import tn.enova.Models.Commons.Attachment;
import tn.enova.Models.Dtos.AttachmentDto;
import tn.enova.Models.Dtos.UserDto;
import tn.enova.Models.Entitys.User;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Services.AuthenticationService;
import tn.enova.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;
    private final AuthenticationService authService;
    @Autowired
    public UserController(@Qualifier("user-service") UserService service,
                          @Qualifier("authentication-service") AuthenticationService authService) {
        this.service = service;
        this.authService = authService;
    }

    @Async("web-client")
    @GetMapping("all-address-mail")
    public CompletableFuture<Set<String>>  getAllAddressMail() {
        return CompletableFuture.completedFuture(this.service.selectAllEmail());
    }

    @GetMapping
    public List<UserDto> GetAll() {
        List<User> users = service.selectAll();
        return users.stream().map(user -> UserMapper.mapToDto(user)).collect(Collectors.toList());
    }
    @GetMapping("/ids/{ids}")
    public List<UserDto> GetAllByIds(@PathVariable(name = "ids") List<String> ids) {
        final List<User> users = service.selectAllByIds(ids);
        return users.stream().map(user -> UserMapper.mapToDto(user)).collect(Collectors.toList());
    }
    @GetMapping("{id}")
    public ResponseEntity<UserDto> selectBy(@PathVariable String id) {
        return ResponseEntity.ok(UserMapper.mapToDto(service.selectById(id)));
    }

    @GetMapping("/username/{usename}")
    public ResponseEntity<UserDto> selectByUsername(@PathVariable String usename) {
        return ResponseEntity.ok(UserMapper.mapToDto(service.selectByUsername(usename)));
    }
    @GetMapping("/authenticate")
    public ResponseEntity<UserDto> selectByAuth() {
        return ResponseEntity.ok(UserMapper.mapToDto(service.selectByUsername(authService.getUsername())));
    }

    @PostMapping
    public UserDto Add(@RequestBody UserDto user) {
        return UserMapper.mapToDto(service.insert(UserMapper.mapToEntity(user)));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDto> Update(@PathVariable String id, @RequestBody UserDto user) {
        return ResponseEntity.ok(UserMapper.mapToDto(service.update(id, UserMapper.mapToEntity(user))));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> Delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete user")
                .message("SUCCESSFUL to delete user : " + id)
                .datestamp(new Date()).build());
    }


    @PutMapping("/photo-to-user/{username}")
    public AttachmentDto updatePhotoProfileToAccount(@RequestParam("file") MultipartFile file, @PathVariable("username") String username) throws Exception {
        Attachment attachment = service.savePhotoToUser(file, username);
        return AttachmentMapper.mapToDto(attachment);
    }

    @GetMapping("/get-photo-by-id/{userId}")
    public ResponseEntity<Resource> downloadPhotobyId(@PathVariable("userId") String userId) throws Exception {
        Attachment attachment = service.getAttachmentbyId(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                                + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @GetMapping("/get-photo-by-username/{username}")
    public ResponseEntity<Resource> downloadPhotobyUsername(@PathVariable("username") String username) throws Exception {
        Attachment attachment = service.getAttachmentbyUsername(username);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                                + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }


    @Async("web-client")
    @PutMapping("/mail-code-forgot-password/{username}/{email}")
    public CompletableFuture<?> sendMailCodeForgotPassword(@PathVariable("username") String username, @PathVariable("email") String email) throws IOException/*, MessagingException*/ {
        return service.sendMailCodeForgotPassword(username, email);
    }

    @PutMapping("/update-forgot-password/{code}/{newpassword}")
    public ResponseEntity<MsgReponseStatus> updateForgotPassword(@PathVariable("code") String code, @PathVariable("newpassword") String newPassword) {
        return ResponseEntity.ok(service.updateForgotPassword(code, newPassword));
    }

    @PutMapping("update-password/{username}/{currentpassword}/{newpassword}")
    public ResponseEntity<MsgReponseStatus> updatePassword(@PathVariable("username") String usename,
                                                           @PathVariable("currentpassword") String currentPassword,
                                                           @PathVariable("newpassword") String newPassword) {
        return ResponseEntity.ok(service.updatePassword(usename, currentPassword, newPassword));
    }


    @PutMapping("/update-role-and-activate/{username}/{role}/{enabled}")
    public ResponseEntity<MsgReponseStatus> updateRoleAndActivate(@PathVariable("username") String username, @PathVariable("role") Roles role, @PathVariable("enabled") boolean enabled) {
        return ResponseEntity.ok(service.updateRoleAndActivate(username, role, enabled));
    }


    @PutMapping("/set-role/{username}/{role}")
    public ResponseEntity<MsgReponseStatus> updateRole(@PathVariable("username") String username, @PathVariable("role") Roles role) {
        return ResponseEntity.ok(service.updateRole(username, role));
    }


    @PutMapping("/set-enable/{username}/{enabled}")
    public ResponseEntity<MsgReponseStatus> activateUser(@PathVariable("username") String username, @PathVariable("enabled") boolean enabled) {
        return ResponseEntity.ok(service.activateUser(username, enabled));
    }


}