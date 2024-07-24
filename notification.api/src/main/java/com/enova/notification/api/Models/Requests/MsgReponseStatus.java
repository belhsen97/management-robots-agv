package com.enova.notification.api.Models.Requests;


import com.enova.notification.api.Enums.ReponseStatus;
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
public class MsgReponseStatus {
    String title;
    Date datestamp;
    ReponseStatus status;
    String message;
}