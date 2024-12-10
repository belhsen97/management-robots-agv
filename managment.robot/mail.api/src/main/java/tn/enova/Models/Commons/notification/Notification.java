package tn.enova.Models.Commons.notification;


import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.enova.Enums.LevelType;

import java.io.Serializable;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification implements Serializable {
    String from;
    String to;
    Long timestamp;
    LevelType level;
    String message;
}