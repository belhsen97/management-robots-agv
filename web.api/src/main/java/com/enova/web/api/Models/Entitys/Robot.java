package com.enova.web.api.Models.Entitys;


import com.enova.web.api.Enums.Connection;
import com.enova.web.api.Enums.ModeRobot;
import com.enova.web.api.Enums.OperationStatus;
import com.enova.web.api.Enums.StatusRobot;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    String  id;
    String name;
    String clientid;
    String username;
    String password;
    Connection connection;
    StatusRobot statusRobot;
    ModeRobot modeRobot;
    OperationStatus operationStatus;
    Date createdAt;
    int levelBattery;  // real time  %
    double speed ; // real time   m/s
    String notice;
    String nameWorkstation;

    @JsonIgnore
    Workstation workstation;
   // public void setWorkstation(Workstation w) {if(w==null){return;}this.idWorkstation = w.getName(); this.workstation = null;   }
    @JsonGetter("workstation")
    public Workstation getWorkstation() {return this.workstation;}
}

//    String id;
//    String name ;
//    String ip;
//    Date createdAt;
//    StatusRobot statusRobot;
//    ModeRobot modeRobot;
//    String notice ;
//    Connection connection;
//    boolean powerOff;
//    StatusBattery statusBattery;
//    double distance ; // real time
//    int levelBattery;  // real time
//    double speed ; // real time
//    String idWorkstation;  enum StatusBattery {CHARGED,DISCHARGED}