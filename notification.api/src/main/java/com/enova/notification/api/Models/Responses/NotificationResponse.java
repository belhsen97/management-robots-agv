package com.enova.notification.api.Models.Responses;

import com.enova.notification.api.Enums.LevelType;
import lombok.*;
import lombok.experimental.FieldDefaults;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse  {
    String id;
    //String sender;
    String senderName;
    String senderImageUrl;
    Long  createdAt;
    LevelType level;
    String message;
}