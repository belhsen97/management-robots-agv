package tn.enova.Controllers;

import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Listener.ListenerTopic;
import tn.enova.Models.Entitys.Robot;
import tn.enova.Services.ObjectMapperService;
import tn.enova.Services.RobotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class RobotTopicController {
    private final ObjectMapperService objMapperService;
    private final RobotService robotService;

    @ListenerTopic(topic = "topic/data/robot/+", qos = 0)
    public void saveAllPropertys(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        String name = parts[3];
        final Robot r = objMapperService.fromJson(new String(message), Robot.class);
        robotService.updateRobot(name,r);
    }
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