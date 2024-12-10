package tn.enova.Models.Responses;


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
public class NotificationResponse implements Serializable {
    @Builder.Default
    Long timestamp = System.currentTimeMillis();
    String from;
    LevelType level;
    String message;
}