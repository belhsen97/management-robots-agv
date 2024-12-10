package tn.enova.Models.Responses;



import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.enova.Enums.ReponseStatus;

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
