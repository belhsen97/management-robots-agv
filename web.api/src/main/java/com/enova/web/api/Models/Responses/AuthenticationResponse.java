package com.enova.web.api.Models.Responses;

import com.enova.web.api.Enums.ReponseStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AuthenticationResponse extends MsgReponseStatus {
   @Builder(builderMethodName = "childBuilder")
   public AuthenticationResponse(String title,
                                    Date datestamp,
                                    ReponseStatus status,
                                    String message,
                                    String token) {
      super(title ,datestamp ,  status , message);
      this.token = token;
   }
   String token;
}
