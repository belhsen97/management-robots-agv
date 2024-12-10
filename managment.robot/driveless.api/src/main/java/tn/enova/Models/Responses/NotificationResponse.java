package tn.enova.Models.Responses;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.enova.Enums.LevelType;
import tn.enova.Models.Commons.Robot;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    @Builder.Default
    Long timestamp = System.currentTimeMillis();
    Robot from;
    LevelType level;
    String message;
}