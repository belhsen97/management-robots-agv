package tn.enova.Controllers;


import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Listener.ListenerTopic;
import tn.enova.Models.Commons.Publish;
import tn.enova.Models.Commons.Robot;
import tn.enova.Services.MQTTService;
import tn.enova.Services.ObjectMapperService;
import tn.enova.Services.RobotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@AllArgsConstructor
public class TopicController {
    private final ObjectMapperService objMapperService;
    private final RobotService robotService;
    private final MQTTService mqttService;


@ListenerTopic(topic = "topic/data/robot/+/property/+", qos = 0) // example topic: topic/robot/data/robot-1/property/MODE_ROBOT
public  void handleRobotTopicMessage(String topic, byte[] message)  throws JsonProcessingException, RessourceNotFoundException {
     //   System.out.println("topic/robot/data/#  : " + new String(message));
    String[] parts = topic.split("/");
    int lengthParts = parts.length;
    if (lengthParts != 6) {throw new IllegalArgumentException("Invalid topic format: " + topic);}
    final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);

    System.out.println(parts[3] + " " +parts[5]  + " " + messageMap.get("value"));
    //    final Robot rDB = robotService.selectByName( parts[3] ); //  parts[3] is name robot
//    final TypeProperty type =TypeProperty.parseType(parts[5]);
//    if (type == null) {throw new IllegalArgumentException("Invalid type property" + type.toString());}
//    RobotProperty robotProperty = RobotProperty.builder().name(rDB.getName()).type(type).timestamp(new Date()).value(messageMap.get("value").toString()).build();

}


    @ListenerTopic(topic = "topic/control/robot/+/last-update", qos = 0)//ex : topic/data/robot/robot-1/last
    public void getLastDataRobotByName(String topic, byte[] message) throws RessourceNotFoundException, MqttException, JsonProcessingException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 5) {throw new IllegalArgumentException("Invalid topic format: " + topic);}
        String nameRobot = parts[3];
        Robot robot =  robotService.selectByName(nameRobot);
        String robotJson =  objMapperService.toJson(robot);
        Publish publish =  Publish.childBuilder().topic("topic/data/robot/"+nameRobot+"/last-update").retained(false).qos(0).payload(robotJson.getBytes()).build();
        mqttService.publish(publish);
    }
}
