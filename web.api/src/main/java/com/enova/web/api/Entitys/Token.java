package com.enova.web.api.Entitys;


import com.enova.web.api.Enums.TokenType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token implements Serializable {
    String token;
    TokenType tokenType = TokenType.BEARER;
    boolean revoked;
    boolean expired;
}
