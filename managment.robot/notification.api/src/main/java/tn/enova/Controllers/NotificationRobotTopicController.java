package tn.enova.Controllers;

import tn.enova.Configures.ParameterConfig;
import tn.enova.Exceptions.NotificationNotFound;
import tn.enova.Listener.ListenerTopic;
import tn.enova.Mappers.NotificationMapper;
import tn.enova.Models.Commons.Publish;
import tn.enova.Models.Entitys.Notification;
import tn.enova.Models.Requests.NotificationRobot;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Services.MQTTService;
import tn.enova.Services.NotificationService;
import tn.enova.Services.ObjectMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationRobotTopicController {
    private final NotificationService notificationService;
    private final ObjectMapperService objMapperService;
    private final MQTTService mqttService;
    private final NotificationMapper notificationMapper;
//topic/notification/robot/+
    @ListenerTopic(topic = "topic/data/service/driveless/notification/robot/name/+", qos = 0)// example topic: topic/robot/data/robot-1/property/MODE_ROBOT
    public void handleRobotTopicMessage(String topic, byte[] message) throws JsonProcessingException, NotificationNotFound , MqttException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 8) {throw new IllegalArgumentException("Invalid topic format of RobotTopicMessage: " + topic);}
        String propertyType = parts[6];
        if (!propertyType.equals("name")) {throw new IllegalArgumentException("Invalid property of robot should be name of RobotTopicMessage: " + topic);}
        String nameRobot = parts[7];
        NotificationRobot notificationRobot = objMapperService.fromJson(new String(message), NotificationRobot.class);
        System.out.println(nameRobot + " " + notificationRobot.toString());
        Notification notification = notificationMapper.mapNotificationRobotToEntity(notificationRobot);
        notificationService.insert(notification);

        NotificationResponse notificationResponse = notificationMapper.mapNotificationRobotToResponse(notificationRobot);

        String notificationResponseJson =  objMapperService.toJson(notificationResponse);
        Publish publish =  Publish.childBuilder()
                        .topic("topic/data/service/notification/robot/name/"+nameRobot)//topic/notification/service/notification
                        .retained(false)
                        .qos(0)
                        .payload(notificationResponseJson.getBytes())
                        .build();
        mqttService.publish(publish);
    }
}
