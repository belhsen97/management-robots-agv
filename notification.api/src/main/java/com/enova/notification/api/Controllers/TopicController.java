package com.enova.notification.api.Controllers;


import com.enova.notification.api.Exceptions.NotificationNotFound;
import com.enova.notification.api.Listener.ListenerTopic;
import com.enova.notification.api.Services.NotificationService;
import com.enova.notification.api.Services.ObjectMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@AllArgsConstructor
public class TopicController {
    private final NotificationService notificationService;
    private final ObjectMapperService objMapperService;
@ListenerTopic(topic = "topic/notification/robot/+", qos = 0) // example topic: topic/robot/data/robot-1/property/MODE_ROBOT
public  void handleRobotTopicMessage(String topic, byte[] message)  throws JsonProcessingException, NotificationNotFound {
     //   System.out.println("topic/robot/data/#  : " + new String(message));
    String[] parts = topic.split("/");
    int lengthParts = parts.length;
    if (lengthParts != 6) {throw new IllegalArgumentException("Invalid topic format: " + topic);}
    final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
    System.out.println(parts[3] + " " +parts[5]  + " " + messageMap.get("value"));
}
}
