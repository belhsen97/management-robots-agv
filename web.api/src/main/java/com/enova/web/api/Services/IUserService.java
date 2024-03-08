package com.enova.web.api.Services;

import com.enova.web.api.Dtos.AuthenticationRequestDto;
import com.enova.web.api.Dtos.MsgReponseStatus;
import com.enova.web.api.Entitys.Attachment;
import com.enova.web.api.Entitys.Enums.Roles;
import com.enova.web.api.Entitys.User;
import com.enova.web.api.Libs.IGenericCRUD;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IUserService extends IGenericCRUD<User,String> {
    User selectByUsername(String username);
    Attachment savePhotoToUser(MultipartFile file, String username ) throws Exception;
    Attachment getAttachmentbyId( String id );
    Attachment getAttachmentbyUsername ( String username );
    MsgReponseStatus register(AuthenticationRequestDto request) throws IOException, MessagingException;





    MsgReponseStatus sendMailCodeForgotPassword(String username , String email) throws IOException, MessagingException;
    MsgReponseStatus updateForgotPassword(String code, String newPassword);

    MsgReponseStatus updatePassword(String username,String currentPassword, String newPassword);
    MsgReponseStatus updateRoleAndActivate(String username , Roles role, boolean enabled);


    MsgReponseStatus updateRole( String username ,   Roles role);
    MsgReponseStatus activateUser ( String username, boolean enabled);

}

