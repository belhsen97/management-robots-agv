package tn.enova.Models.Requests;


import tn.enova.Enums.LevelType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationUser  implements Serializable {
    Long timestamp;
    String from;
    LevelType level;
    String message;
}
