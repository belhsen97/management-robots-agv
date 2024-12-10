package tn.enova.Models.Responses;

import tn.enova.Enums.LevelType;
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
    String senderName;
    String senderImageUrl;
    Long  createdAt;
    LevelType level;
    String message;
}