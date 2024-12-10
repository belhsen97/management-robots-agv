package tn.enova.Models.Commons;



import tn.enova.Enums.Connection;
import tn.enova.Enums.ModeRobot;
import tn.enova.Enums.OperationStatus;
import tn.enova.Enums.StatusRobot;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Robot implements Serializable {
    @Setter(AccessLevel.NONE)
    String id;
    String name;
    String clientid;
    String username;
    String password;
    String notice;
    Date createdAt;

    String codeTag;  // real time
    Connection connection;  // real time
    StatusRobot statusRobot;  // real time
    ModeRobot modeRobot;  // real time
    OperationStatus operationStatus;  // real time
    double levelBattery;  // real time  %
    double speed; // real time   m/s
    double distance; // real time   m
}