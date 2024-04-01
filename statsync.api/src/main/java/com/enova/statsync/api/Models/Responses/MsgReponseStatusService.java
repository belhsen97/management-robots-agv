package com.enova.statsync.api.Models.Responses;

import com.enova.statsync.api.Enums.ReponseStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MsgReponseStatusService extends MsgReponseStatus {
   @Builder(builderMethodName = "childBuilder")
   public MsgReponseStatusService(String title,
                                 Date datestamp,
                                 ReponseStatus status,
                                 String message,
                                 String nameService) {
      super(title ,datestamp ,  status , message);
      this.nameService = nameService;
   }
   String nameService;
}
