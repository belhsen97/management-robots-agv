package tn.enova.Models.Entitys;


import tn.enova.Enums.TokenType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token implements Serializable {
    String token;
    TokenType tokenType = TokenType.BEARER;
    boolean revoked;
    boolean expired;
}
