package com.enova.collector.api.Controllers;

import com.enova.collector.api.Enums.Connection;
import com.enova.collector.api.Enums.TypeProperty;
import com.enova.collector.api.Exceptions.RessourceNotFoundException;
import com.enova.collector.api.Listener.ListenerTopic;
import com.enova.collector.api.Mappers.RobotMapper;
import com.enova.collector.api.Models.Commons.ConnectionInfo;
import com.enova.collector.api.Models.Entitys.Robot;
import com.enova.collector.api.Models.Entitys.RobotProperty;
import com.enova.collector.api.Services.ObjectMapperService;
import com.enova.collector.api.Services.RobotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
            List<RobotProperty> listPropertys = Arrays.asList(RobotProperty.builder().timestamp(new Date(connectionInfo.getConnectedAt())).name(rDB.getName()).type(TypeProperty.CONNECTION).value(Connection.CONNECTED.name()).build());
            robotService.insertDataPropertys(listPropertys);
            System.out.println(listPropertys);
        }
        if (connectionInfo.getConnectedAt() < connectionInfo.getDisconnectedAt()) {

            robotService.updateRobotConnection(connectionInfo.getClientId(), Connection.DISCONNECTED);
            List<RobotProperty> listPropertys = Arrays.asList(RobotProperty.builder().timestamp(new Date(connectionInfo.getDisconnectedAt())).name(rDB.getName()).type(TypeProperty.CONNECTION).value(Connection.DISCONNECTED.name()).build());
            robotService.insertDataPropertys(listPropertys);
            System.out.println(listPropertys);
        }
    }

    @ListenerTopic(topic = "topic/robot/data/#", qos = 0)
    public void getAllData(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        //        System.out.println("topic: " + topic);
//        System.out.println("Qos: " + message.getQos());
        //System.out.println("message content: " + new String(message.getPayload()));
        //listenerManager.handleMessage(topic, message.getPayload());
        // System.out.println("Async task started "+topic);

        final Robot r = objMapperService.fromJson(new String(message), Robot.class);
        final Robot rDB = robotService.selectByName(r.getName());
        robotService.updateRobot(rDB, r);
        List<RobotProperty> listPropertys = RobotMapper.convertToRobotPropertyList(r);
        robotService.insertDataPropertys(listPropertys);
    }

}
