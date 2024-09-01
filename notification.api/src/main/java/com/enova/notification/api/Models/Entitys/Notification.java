package com.enova.notification.api.Models.Entitys;

import com.enova.notification.api.Enums.LevelType;
import com.enova.notification.api.Enums.TypeSender;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "notification")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification implements Serializable {
    @Id
    String id;
    TypeSender typeSender;
    String sender;
    Long  createdAt;
    Long  expiredAt;
    String displayType;//"WEB" "MAIL" "WhatsApp"
    LevelType level;
    String message;
    List<String> viewedByUserIds;
}