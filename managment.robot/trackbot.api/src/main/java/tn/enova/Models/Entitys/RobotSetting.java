package tn.enova.Models.Entitys;

import tn.enova.Enums.Constraint;
import tn.enova.Enums.TypeProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "robotsetting")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RobotSetting {
    @Id
    String id;
    TypeProperty category;
    Constraint constraint;
    String value;
    String unit;
}