package com.enova.web.api.Models.Responses;


import com.enova.web.api.Enums.Connection;
import com.enova.web.api.Enums.ModeRobot;
import com.enova.web.api.Enums.OperationStatus;
import com.enova.web.api.Enums.StatusRobot;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RobotData {
    String id;
    String name;

    int timestamp; //number of milliseconds
    Connection connection;
    StatusRobot statusRobot;
    ModeRobot modeRobot;
    OperationStatus operationStatus;


    Object[][] serieSpeed = {{1647523800000L, 140.785},};
    Object[][] serieBattery = { {1647523800000L,90.5},};
}
