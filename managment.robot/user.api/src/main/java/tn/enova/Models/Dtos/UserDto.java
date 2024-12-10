package tn.enova.Models.Dtos;

import tn.enova.Enums.Gender;
import tn.enova.Enums.Roles;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;


@Builder
@AllArgsConstructor
@NoArgsConstructor
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
