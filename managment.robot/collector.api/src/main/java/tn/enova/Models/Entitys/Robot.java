package tn.enova.Models.Entitys;


import tn.enova.Enums.Connection;
import tn.enova.Enums.ModeRobot;
import tn.enova.Enums.OperationStatus;
import tn.enova.Enums.StatusRobot;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "robot")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Robot implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    String id;
    String name;
    @Setter(AccessLevel.NONE)
    String clientid;
    @Setter(AccessLevel.NONE)
    String username;
    @Setter(AccessLevel.NONE)
    String password;
    @Setter(AccessLevel.NONE)
    String notice;
    @Setter(AccessLevel.NONE)
    Date createdAt;

    String codeTag;  // real time
    Connection connection;  // real time
    StatusRobot statusRobot;  // real time
    ModeRobot modeRobot;  // real time
    OperationStatus operationStatus;  // real time
    double levelBattery;  // real time  %
    double speed; // real time   m/s
    double distance; // real time   m
//    public void setSpeed(double speed) {
//        this.speed = Math.round(speed * 10.0) / 10.0;
//    }
}