package com.enova.driveless.api.Controllers;

import com.enova.driveless.api.Listener.ListenerTopic;
import com.enova.driveless.api.Models.Commons.ConnectionInfo;
import com.enova.driveless.api.Services.ObjectMapperService;
import com.enova.driveless.api.Services.RobotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor

public class TopicController {
    private final ObjectMapperService objMapperService;
    final RobotService robotService;
    @ListenerTopic(topic = "$SYS/brokers/+/clients/#",qos = 0)
    public  void handleTopicStatusClient(String topic,byte[] message) throws JsonProcessingException {
        System.out.println("topic : " + topic);
        System.out.println("$SYS/brokers/+/clients/#  : " + new String(message));
        ConnectionInfo connectionInfo = objMapperService.fromJson(new String(message), ConnectionInfo.class);
       // System.out.println("$SYS/brokers/+/clients/#  : " + connectionInfo.toString());
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
