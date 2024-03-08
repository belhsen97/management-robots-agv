package com.enova.web.api.Dtos;


import com.enova.web.api.Entitys.Enums.Connection;
import com.enova.web.api.Entitys.Enums.ModeRobot;
import com.enova.web.api.Entitys.Enums.OperationStatus;
import com.enova.web.api.Entitys.Enums.StatusRobot;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RobotDto {
    String id;
    String name ;
    Connection connection;
    StatusRobot statusRobot;
    ModeRobot modeRobot;
    OperationStatus operationStatus;
    Date createdAt;
    int levelBattery;  // real time
    double speed ; // real time
    WorkstationDto workstation;
    String notice;
}