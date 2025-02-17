package tn.enova.Controllers;



import tn.enova.Enums.TypeProperty;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Listener.ListenerTopic;
import tn.enova.Models.Commons.ConnectionInfo;
import tn.enova.Services.ObjectMapperService;
import tn.enova.Services.RobotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class PropertyTopicController {
    private final RobotService robotService;
    private final ObjectMapperService objMapperService;

    @ListenerTopic(topic = "$SYS/brokers/+/clients/#", qos = 0)
    public void handleTopicStatusClient(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        ConnectionInfo connectionInfo = objMapperService.fromJson(new String(message), ConnectionInfo.class);
        robotService.updateConnection(connectionInfo);
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/STATUS_ROBOT", qos = 0)
    public void getPropertyStatusRobot(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Status Robot format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        final TypeProperty type = TypeProperty.parseType(parts[5]);
        if (type == null) {
            throw new IllegalArgumentException("Invalid type property Status Robot" + type.toString());
        }
        robotService.updateStatusRobot(parts[3], messageMap.get("value"));
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/MODE_ROBOT", qos = 0)
    public void getPropertyModeRobot(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Mode Robot format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        robotService.updateModeRobot(parts[3], messageMap.get("value"));
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/OPERATION_STATUS", qos = 0)
    public void getPropertyOperationStatus(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Operation Status format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        robotService.updateOperationStatus(parts[3], messageMap.get("value"));
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/LEVEL_BATTERY", qos = 0)
    public void getPropertyLevelBattery(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Level Battery format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        robotService.updateLevelBattery(parts[3], messageMap.get("value"));
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/SPEED", qos = 0)
    public void getPropertySpeed(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Speed format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        robotService.updateSpeed(parts[3], messageMap.get("value"));
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/DISTANCE", qos = 0)
    public void getPropertyDistance(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Distance format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        robotService.updateDistance(parts[3], messageMap.get("value"));
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/TAGCODE", qos = 0)
    public void getPropertyTagCode(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Tag Code format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        robotService.updateTagCode(parts[3], messageMap.get("value"));
    }
}
//    @ListenerTopic(topic = "topic/data/robot/+/property/CONNECTION", qos = 0)
//    public void getPropertyConnection(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
//        String[] parts = topic.split("/");
//        int lengthParts = parts.length;
//        if (lengthParts != 6) {
//            throw new IllegalArgumentException("Invalid topic Connection format: " + topic);
//        }
//        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
//        robotService.updateConnection(parts[3], messageMap.get("value"));
//    }