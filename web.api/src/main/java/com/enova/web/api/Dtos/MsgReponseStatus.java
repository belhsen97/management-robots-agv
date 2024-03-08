package com.enova.web.api.Dtos;


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
    String title = "Message";
    Date datestamp;
    ReponseStatus status;
    String message;
}
