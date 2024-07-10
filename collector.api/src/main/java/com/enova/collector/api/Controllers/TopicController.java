package com.enova.collector.api.Controllers;

import com.enova.collector.api.Enums.Connection;
import com.enova.collector.api.Enums.TypeProperty;
import com.enova.collector.api.Exceptions.RessourceNotFoundException;
import com.enova.collector.api.Listener.ListenerTopic;
import com.enova.collector.api.Models.Commons.ConnectionInfo;
import com.enova.collector.api.Models.Entitys.Robot;
import com.enova.collector.api.Models.Entitys.RobotProperty;
import com.enova.collector.api.Services.ObjectMapperService;
import com.enova.collector.api.Services.RobotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;


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
            RobotProperty robotProperty = RobotProperty.builder().timestamp(new Date(connectionInfo.getConnectedAt())).name(rDB.getName()).type(TypeProperty.CONNECTION).value(Connection.CONNECTED.name()).build();
            robotService.insertPropertyRobot(robotProperty);
        }
        if (connectionInfo.getConnectedAt() < connectionInfo.getDisconnectedAt()) {
            robotService.updateRobotConnection(connectionInfo.getClientId(), Connection.DISCONNECTED);
            RobotProperty robotProperty = RobotProperty.builder().timestamp(new Date(connectionInfo.getDisconnectedAt())).name(rDB.getName()).type(TypeProperty.CONNECTION).value(Connection.DISCONNECTED.name()).build();
            robotService.insertPropertyRobot(robotProperty);

        }
    }

    @ListenerTopic(topic = "topic/data/robot/+", qos = 0)
    public void saveAllPropertys(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        String name = parts[3];
        //System.out.println("topic: " + topic);
        //System.out.println("Qos: " + message.getQos());
        //System.out.println("message content: " + new String(message.getPayload()));
        final Robot r = objMapperService.fromJson(new String(message), Robot.class);
        robotService.updateRobot(name,r);
    }
// @ListenerTopic(topic = "topic/data/robot/+/property/+", qos = 0) // example topic: topic/robot/data/robot-1/property/MODE_ROBOT
 //   public void saveProperty(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
//        String[] parts = topic.split("/");
//        int lengthParts = parts.length;
//        if (lengthParts != 6) {throw new IllegalArgumentException("Invalid topic format: " + topic);}
//        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
//        final Robot rDB = robotService.selectByName( parts[3] ); //  parts[3] is name robot
//        final TypeProperty type =TypeProperty.parseType(parts[5]);
//        if (type == null) {throw new IllegalArgumentException("Invalid type property" + type.toString());}
//        RobotProperty robotProperty = RobotProperty.builder().name(rDB.getName()).type(type).timestamp(new Date()).value(messageMap.get("value").toString()).build();
//        robotService.insertPropertyRobot(robotProperty);
//        RobotMapper.mapRobotPropertyToRobot(rDB ,robotProperty);
//    }
}
