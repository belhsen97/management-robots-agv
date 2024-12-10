package tn.enova.Models.Responses;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.enova.Enums.LevelType;
import tn.enova.Models.Entitys.Robot;

import java.io.Serializable;
import java.util.Objects;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse implements Serializable {
    @Builder.Default
    Long timestamp = System.currentTimeMillis();
    String from;
    LevelType level;
    String message;
}