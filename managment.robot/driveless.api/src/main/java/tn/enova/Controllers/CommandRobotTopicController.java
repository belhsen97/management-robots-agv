package tn.enova.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tn.enova.Enums.ModeRobot;
import tn.enova.Enums.OperationStatus;
import tn.enova.Enums.StatusRobot;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Listener.ListenerTopic;
import tn.enova.Services.ObjectMapperService;
import tn.enova.Services.RobotService;

import java.util.Map;

@Component
@AllArgsConstructor
public class CommandRobotTopicController {
    private final ObjectMapperService objMapperService;
    private final RobotService robotService;
    @ListenerTopic(topic = "topic/control/driveless/robot/all/MODE_ROBOT", qos = 0)
    public void setPropertyModeRobotByName(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid Topic Control Robot Format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        System.out.println(new String(message));
        String valueString = (String) messageMap.get("value");
        robotService.updateAllRobotsMode(ModeRobot.valueOf(valueString.toUpperCase()));
    }
    @ListenerTopic(topic = "topic/control/driveless/robot/all/OPERATION_STATUS", qos = 0)
    public void setPropertyOperationStatusByName(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid Topic Control Robot Format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        System.out.println(new String(message));
        String valueString = (String) messageMap.get("value");
        robotService.updateAllRobotsOperationStatus(OperationStatus.valueOf(valueString.toUpperCase()));

    }
    @ListenerTopic(topic = "topic/control/robot/all/property/STATUS", qos = 0)
    public void setPropertyStatusByName(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid Topic Control Robot Format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
     System.out.println(new String(message));
    }
    @ListenerTopic(topic = "topic/control/driveless/robot/all/SPEED", qos = 0)
    public void setPropertySpeedByName(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid Topic Control Robot Format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        System.out.println(new String(message));
        robotService.updateAllRobotsSpeed( (Long) messageMap.get("value"));

    }
}
