package com.enova.web.api.Models.Dtos;

import com.enova.web.api.Enums.Gender;
import com.enova.web.api.Enums.Roles;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;


@Builder
@AllArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String id;
    Date createdAt;
    String username;
    Roles role;
    boolean enabled;
    String code;
    String firstname;
    String lastname;
    String matricule;
    int phone;
    String email;
    Gender gender;
    AttachmentDto photo;
}
