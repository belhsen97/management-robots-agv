package tn.enova.Models.Responses;

import tn.enova.Enums.StatusRobotAuth;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
       StatusRobotAuth result;
       boolean is_superuser;
}