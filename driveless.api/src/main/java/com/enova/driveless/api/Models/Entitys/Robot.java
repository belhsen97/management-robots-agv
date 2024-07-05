package com.enova.driveless.api.Models.Entitys;



import com.enova.driveless.api.Enums.Connection;
import com.enova.driveless.api.Enums.ModeRobot;
import com.enova.driveless.api.Enums.OperationStatus;
import com.enova.driveless.api.Enums.StatusRobot;
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
    @Setter(AccessLevel.NONE)
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
    String nameWorkstation;
    Connection connection;
    StatusRobot statusRobot;
    ModeRobot modeRobot;
    OperationStatus operationStatus;
    Date createdAt;
    int levelBattery;  // real time  %
    double speed; // real time   m/s
    //double distance; // real time   m
    public void setSpeed(double speed) {
        this.speed = Math.round(speed * 10.0) / 10.0;
    }
}