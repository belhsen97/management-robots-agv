package tn.enova.Models.Commons;


import tn.enova.Enums.Constraint;
import tn.enova.Enums.TypeProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RobotSetting {
     String id;
     TypeProperty category;
     Constraint constraint;
     String value;
     String unit;
}

