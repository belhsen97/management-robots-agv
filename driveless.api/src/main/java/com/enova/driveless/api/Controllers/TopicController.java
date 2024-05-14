package com.enova.driveless.api.Controllers;

import com.enova.driveless.api.Enums.Connection;
import com.enova.driveless.api.Exceptions.RessourceNotFoundException;
import com.enova.driveless.api.Listener.ListenerTopic;
import com.enova.driveless.api.Models.Commons.ConnectionInfo;
import com.enova.driveless.api.Models.Entitys.Robot;
import com.enova.driveless.api.Models.Entitys.RobotProperty;
import com.enova.driveless.api.Services.ObjectMapperService;
import com.enova.driveless.api.Services.RobotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
@AllArgsConstructor
public class TopicController {
    private final ObjectMapperService objMapperService;
    private final RobotService robotService;
    @ListenerTopic(topic = "$SYS/brokers/+/clients/#", qos = 0)
    public void handleTopicStatusClient(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        ConnectionInfo connectionInfo = objMapperService.fromJson(new String(message), ConnectionInfo.class);
        final Robot rDB = robotService.selectByClientId(connectionInfo.getClientId());

        if (connectionInfo.getConnectedAt() > connectionInfo.getDisconnectedAt()) {
            robotService.updateRobotConnection(connectionInfo.getClientId(), Connection.CONNECTED);
        }
        if (connectionInfo.getConnectedAt() < connectionInfo.getDisconnectedAt()) {
            robotService.updateRobotConnection(connectionInfo.getClientId(), Connection.DISCONNECTED);
        }
    }

    //    @ListenerTopic(topic = "topic/robot/data/robot-3",qos = 0)
//    public  void handleRobotTopicMessage(byte[]  message) {
//        System.out.println("topic/robot/data/robot-3 (1) : " + new String(message));
//    }
//    @ListenerTopic(topic = "topic/robot/data/#",qos = 0)
//    public  void handleRobot3TopicMessage(byte[] message) {
//        System.out.println("topic/robot/data/#  : " + new String(message));
//    }

}
