package tn.enova.Models.Entitys;

import tn.enova.Enums.LevelType;
import tn.enova.Enums.TypeSender;
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
    String sender;//senderId
    Long  createdAt;
    Long  expiredAt;
    String displayType;//"WEB" "MAIL" "WhatsApp"
    LevelType level;
    String message;
    List<String> viewedByUserIds;
}