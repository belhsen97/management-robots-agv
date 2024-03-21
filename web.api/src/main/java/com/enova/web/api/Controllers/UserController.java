package com.enova.web.api.Controllers;


import com.enova.web.api.Dtos.*;
import com.enova.web.api.Entitys.Attachment;
import com.enova.web.api.Enums.Roles;
import com.enova.web.api.Entitys.User;
import com.enova.web.api.Mappers.AttachmentMapper;
import com.enova.web.api.Mappers.UserMapper;
import com.enova.web.api.Services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    private final IUserService iUserService;

    @Autowired
    public UserController(@Qualifier("user-service") IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @GetMapping
    public List<UserDto> SelectAll() {
        List<User> users = iUserService.selectAll();
        return users.stream().map(user -> UserMapper.mapToDto(user)).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> SelectBy(@PathVariable String id) {
        return ResponseEntity.ok(UserMapper.mapToDto(iUserService.selectById(id)));
    }

    @GetMapping("/username/{usename}")
    public ResponseEntity<UserDto> SelectByUsername(@PathVariable String usename) {
        return ResponseEntity.ok(UserMapper.mapToDto(iUserService.selectByUsername(usename)));
    }

    @PostMapping
    public UserDto Insert(@RequestBody UserDto user) {
        return UserMapper.mapToDto(iUserService.insert(UserMapper.mapToEntity(user)));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDto> update(@PathVariable String id, @RequestBody UserDto user) {
        return ResponseEntity.ok(UserMapper.mapToDto(iUserService.update(id, UserMapper.mapToEntity(user))));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> delete(@PathVariable String id) {
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
    public ResponseEntity<Resource> downloadFilebyId(@PathVariable("userId") String userId) throws Exception {
        Attachment attachment = iUserService.getAttachmentbyId(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                                + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @GetMapping("/get-photo-by-username/{username}")
    public ResponseEntity<Resource> downloadFilebyUsername(@PathVariable("username") String username) throws Exception {
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
    public ResponseEntity<MsgReponseStatus> ActivateUser(@PathVariable("username") String username, @PathVariable("enabled") boolean enabled) {
        return ResponseEntity.ok(iUserService.activateUser(username, enabled));
    }


}