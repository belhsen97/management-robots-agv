package tn.enova.Models.Requests;

import tn.enova.Enums.LevelType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRobot {
    Long timestamp;
    RobotRequest from;
    LevelType level;
    String message;
}