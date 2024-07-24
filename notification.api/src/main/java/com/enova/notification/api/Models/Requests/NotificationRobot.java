package com.enova.notification.api.Models.Requests;

import com.enova.notification.api.Enums.LevelType;
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
    String asctime;
    String name;
    LevelType level;
    String message;
}