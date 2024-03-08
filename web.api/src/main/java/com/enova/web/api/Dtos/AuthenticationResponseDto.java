package com.enova.web.api.Dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AuthenticationResponseDto extends MsgReponseStatus {
   @Builder(builderMethodName = "childBuilder")
   public AuthenticationResponseDto(String title,
                                    Date datestamp,
                                    ReponseStatus status,
                                    String message,
                                    String token) {
      super(title ,datestamp ,  status , message);
      this.token = token;
   }
   String token;
}
