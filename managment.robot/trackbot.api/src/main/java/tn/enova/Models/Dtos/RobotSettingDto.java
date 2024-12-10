package tn.enova.Models.Dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RobotSettingDto {
    Map<String, Double> distance =  new HashMap<String, Double>() {{
        put("min", 0D);
        put("max", 0D);
    }};
   Map<String, Double> speed =  new HashMap<String, Double>() {{
        put("min", 0D);
        put("max", 0D);
    }};

        //= Map.of(
//            Constraint.MIN, 0D,
//            Constraint.MAX, 0D
//    );
}