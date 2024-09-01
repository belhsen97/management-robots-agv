package com.enova.web.api.Models.Entitys;

import com.enova.web.api.Enums.Connection;
import com.enova.web.api.Enums.ModeRobot;
import com.enova.web.api.Enums.OperationStatus;
import com.enova.web.api.Enums.StatusRobot;
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
