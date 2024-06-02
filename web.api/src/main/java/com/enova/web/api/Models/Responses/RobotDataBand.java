package com.enova.web.api.Models.Responses;

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
public class RobotDataBand {
    String name;
    Map<String, Object> connection = new HashMap<>();
    Map<String, Object> mode = new HashMap<>();
    Map<String, Object> operationStatus = new HashMap<>();
    Map<String, Object> statusRobot = new HashMap<>();
    Map<String, Object> battery = new HashMap<>();
    Map<String, Object> speed = new HashMap<>();
}