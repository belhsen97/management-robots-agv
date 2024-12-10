package tn.enova.Services;

import tn.enova.Enums.Roles;
import tn.enova.Models.Commons.Attachment;
import tn.enova.Models.Entitys.User;
import tn.enova.Models.Responses.MsgReponseStatus;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface UserService extends IGenericCRUD<User,String> {
    List<User> selectAllByIds(List<String> ids);
    Set<String> selectAllEmail() ;

    User selectByUsername(String username);
    Attachment savePhotoToUser(MultipartFile file, String username ) throws Exception;
    Attachment getAttachmentbyId( String id );
    Attachment getAttachmentbyUsername ( String username );


    CompletableFuture<?> sendMailCodeForgotPassword(String username , String email) throws IOException;
    MsgReponseStatus updateForgotPassword(String code, String newPassword);

    MsgReponseStatus updatePassword(String username,String currentPassword, String newPassword);
    MsgReponseStatus updateRoleAndActivate(String username , Roles role, boolean enabled);


    MsgReponseStatus updateRole( String username ,   Roles role);
    MsgReponseStatus activateUser ( String username, boolean enabled);

}

