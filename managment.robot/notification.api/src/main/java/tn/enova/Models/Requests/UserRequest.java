package tn.enova.Models.Requests;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.enova.Enums.Roles;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String id;
    String username;
    Roles role;
    boolean enabled;
    String firstname;
    String lastname;
    String matricule;
    int phone;
    String email;
    AttachmentRequest photo;
}
