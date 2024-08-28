package com.enova.notification.api.Models.Requests;

import com.enova.notification.api.Configures.AsctimeDeserializer;
import com.enova.notification.api.Configures.AsctimeSerializer;
import com.enova.notification.api.Enums.LevelType;
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
    @JsonSerialize(using = AsctimeSerializer.class)
    @JsonDeserialize(using = AsctimeDeserializer.class)
    Long asctime;
    String name;
    LevelType level;
    String message;
}