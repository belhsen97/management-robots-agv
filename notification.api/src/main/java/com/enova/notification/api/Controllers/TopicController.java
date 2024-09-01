package com.enova.notification.api.Controllers;

import com.enova.notification.api.Exceptions.NotificationNotFound;
import com.enova.notification.api.Listener.ListenerTopic;
import com.enova.notification.api.Models.Commons.Publish;
import com.enova.notification.api.Models.Requests.NotificationRobot;
import com.enova.notification.api.Models.Responses.NotificationResponse;
import com.enova.notification.api.Services.MQTTService;
import com.enova.notification.api.Services.NotificationService;
import com.enova.notification.api.Services.ObjectMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class TopicController {
    private final NotificationService notificationService;
    private final ObjectMapperService objMapperService;
    private final MQTTService mqttService;

    @ListenerTopic(topic = "topic/notification/robot/+", qos = 0)// example topic: topic/robot/data/robot-1/property/MODE_ROBOT
    public void handleRobotTopicMessage(String topic, byte[] message) throws JsonProcessingException, NotificationNotFound , MqttException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 4) {throw new IllegalArgumentException("Invalid topic format: " + topic);}
        String nameRobot = parts[3];
        NotificationRobot notificationRobot = objMapperService.fromJson(new String(message), NotificationRobot.class);
        System.out.println(parts[3] + " " + notificationRobot.toString());
        NotificationResponse notificationResponse = NotificationResponse.builder().senderName(nameRobot)
                                                                                  .senderImageUrl("/assets/images/robots/AGV-AMR.png")
                                                                                  .createdAt(notificationRobot.getAsctime())
                                                                                  .level(notificationRobot.getLevel())
                                                                                  .message(notificationRobot.getMessage())
                                                                                  .build();
        String notificationResponseJson =  objMapperService.toJson(notificationResponse);
        Publish publish =  Publish.childBuilder().topic("topic/notification/service/notification").retained(false).qos(0).payload(notificationResponseJson.getBytes()).build();
        mqttService.publish(publish);
    }
}
