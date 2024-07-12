package com.enova.web.api.Controllers;



import com.enova.web.api.Models.Dtos.AttachmentDto;
import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Enums.ReponseStatus;
import com.enova.web.api.Models.Dtos.UserDto;
import com.enova.web.api.Models.Commons.Attachment;
import com.enova.web.api.Enums.Roles;
import com.enova.web.api.Models.Entitys.User;
import com.enova.web.api.Mappers.AttachmentMapper;
import com.enova.web.api.Mappers.UserMapper;
import com.enova.web.api.Services.AuthenticationService;
import com.enova.web.api.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService iUserService;
    private final AuthenticationService authService;
    @Autowired
    public UserController(@Qualifier("user-service") UserService iUserService,
                          @Qualifier("authentication-service") AuthenticationService authService
                          ) {
        this.iUserService = iUserService;
        this.authService = authService;
    }

    @GetMapping
    public List<UserDto> GetAll() {
        List<User> users = iUserService.selectAll();
        return users.stream().map(user -> UserMapper.mapToDto(user)).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> selectBy(@PathVariable String id) {
        return ResponseEntity.ok(UserMapper.mapToDto(iUserService.selectById(id)));
    }

    @GetMapping("/username/{usename}")
    public ResponseEntity<UserDto> selectByUsername(@PathVariable String usename) {
        return ResponseEntity.ok(UserMapper.mapToDto(iUserService.selectByUsername(usename)));
    }
    @GetMapping("/authenticate")
    public ResponseEntity<UserDto> selectByAuth() {
        return ResponseEntity.ok(UserMapper.mapToDto(iUserService.selectByUsername(authService.getUsername())));
    }
    @PostMapping
    public UserDto Add(@RequestBody UserDto user) {
        return UserMapper.mapToDto(iUserService.insert(UserMapper.mapToEntity(user)));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDto> Update(@PathVariable String id, @RequestBody UserDto user) {
        return ResponseEntity.ok(UserMapper.mapToDto(iUserService.update(id, UserMapper.mapToEntity(user))));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> Delete(@PathVariable String id) {
        iUserService.delete(id);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete user")
                .message("SUCCESSFUL to delete user : " + id)
                .datestamp(new Date()).build());
    }


    @PutMapping("/photo-to-user/{username}")
    public AttachmentDto updatePhotoProfileToAccount(@RequestParam("file") MultipartFile file, @PathVariable("username") String username) throws Exception {
        Attachment attachment = iUserService.savePhotoToUser(file, username);
        return AttachmentMapper.mapToDto(attachment);
    }

    @GetMapping("/get-photo-by-id/{userId}")
    public ResponseEntity<Resource> downloadPhotobyId(@PathVariable("userId") String userId) throws Exception {
        Attachment attachment = iUserService.getAttachmentbyId(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                                + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @GetMapping("/get-photo-by-username/{username}")
    public ResponseEntity<Resource> downloadPhotobyUsername(@PathVariable("username") String username) throws Exception {
        Attachment attachment = iUserService.getAttachmentbyUsername(username);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                                + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }



    @PutMapping("/mail-code-forgot-password/{username}/{email}")
    public ResponseEntity<MsgReponseStatus> sendMailCodeForgotPassword(@PathVariable("username") String username, @PathVariable("email") String email) throws IOException, MessagingException {
        return ResponseEntity.ok(iUserService.sendMailCodeForgotPassword(username, email));
    }

    @PutMapping("/update-forgot-password/{code}/{newpassword}")
    public ResponseEntity<MsgReponseStatus> updateForgotPassword(@PathVariable("code") String code, @PathVariable("newpassword") String newPassword) {
        return ResponseEntity.ok(iUserService.updateForgotPassword(code, newPassword));
    }

    @PutMapping("update-password/{username}/{currentpassword}/{newpassword}")
    public ResponseEntity<MsgReponseStatus> updatePassword(@PathVariable("username") String usename,
                                                           @PathVariable("currentpassword") String currentPassword,
                                                           @PathVariable("newpassword") String newPassword) {
        return ResponseEntity.ok(iUserService.updatePassword(usename, currentPassword, newPassword));
    }


    @PutMapping("/update-role-and-activate/{username}/{role}/{enabled}")
    public ResponseEntity<MsgReponseStatus> updateRoleAndActivate(@PathVariable("username") String username, @PathVariable("role") Roles role, @PathVariable("enabled") boolean enabled) {
        return ResponseEntity.ok(iUserService.updateRoleAndActivate(username, role, enabled));
    }


    @PutMapping("/set-role/{username}/{role}")
    public ResponseEntity<MsgReponseStatus> updateRole(@PathVariable("username") String username, @PathVariable("role") Roles role) {
        return ResponseEntity.ok(iUserService.updateRole(username, role));
    }


    @PutMapping("/set-enable/{username}/{enabled}")
    public ResponseEntity<MsgReponseStatus> activateUser(@PathVariable("username") String username, @PathVariable("enabled") boolean enabled) {
        return ResponseEntity.ok(iUserService.activateUser(username, enabled));
    }


}