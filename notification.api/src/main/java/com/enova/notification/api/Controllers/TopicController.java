package com.enova.notification.api.Controllers;

import com.enova.notification.api.Exceptions.NotificationNotFound;
import com.enova.notification.api.Listener.ListenerTopic;
import com.enova.notification.api.Models.Requests.NotificationRobot;
import com.enova.notification.api.Services.NotificationService;
import com.enova.notification.api.Services.ObjectMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TopicController {
    private final NotificationService notificationService;
    private final ObjectMapperService objMapperService;

    @ListenerTopic(topic = "topic/notification/robot/+", qos = 0)
    // example topic: topic/robot/data/robot-1/property/MODE_ROBOT
    public void handleRobotTopicMessage(String topic, byte[] message) throws JsonProcessingException, NotificationNotFound {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 4) {
            throw new IllegalArgumentException("Invalid topic format: " + topic);
        }
        NotificationRobot notificationRobot = objMapperService.fromJson(new String(message), NotificationRobot.class);
        System.out.println(parts[3] + " " + notificationRobot.toString());
    }
}
