package com.enova.web.api.Services;

import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Models.Entitys.Attachment;
import com.enova.web.api.Enums.Roles;
import com.enova.web.api.Models.Entitys.User;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface UserService extends IGenericCRUD<User,String> {
    Set<String> selectAllEmail() ;

    User selectByUsername(String username);
    Attachment savePhotoToUser(MultipartFile file, String username ) throws Exception;
    Attachment getAttachmentbyId( String id );
    Attachment getAttachmentbyUsername ( String username );


    MsgReponseStatus sendMailCodeForgotPassword(String username , String email) throws IOException, MessagingException;
    MsgReponseStatus updateForgotPassword(String code, String newPassword);

    MsgReponseStatus updatePassword(String username,String currentPassword, String newPassword);
    MsgReponseStatus updateRoleAndActivate(String username , Roles role, boolean enabled);


    MsgReponseStatus updateRole( String username ,   Roles role);
    MsgReponseStatus activateUser ( String username, boolean enabled);

}

