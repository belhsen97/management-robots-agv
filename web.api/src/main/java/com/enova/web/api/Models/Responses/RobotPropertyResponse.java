package com.enova.web.api.Models.Responses;


import com.enova.web.api.Enums.TypeProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Date;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RobotPropertyResponse {
    String name;
    long timestamp;
    TypeProperty type;
    String value;
}
