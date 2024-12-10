package tn.enova.Models.Dtos;


import tn.enova.Enums.Connection;
import tn.enova.Enums.ModeRobot;
import tn.enova.Enums.OperationStatus;
import tn.enova.Enums.StatusRobot;
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
    String name;
    String clientid;
    String username;
    String password;
    Date createdAt;
    String notice;


    Connection connection;  // real time
    StatusRobot statusRobot;  // real time
    ModeRobot modeRobot;  // real time
    OperationStatus operationStatus;  // real time
    double levelBattery;  // real time  %
    double speed; // real time   m/s
    double distance; // real time   m
    String codeTag;  // real time
}