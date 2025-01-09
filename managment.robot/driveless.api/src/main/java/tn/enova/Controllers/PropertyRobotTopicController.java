package tn.enova.Controllers;

import tn.enova.Enums.*;
import tn.enova.Exceptions.RessourceNotFoundException;
import tn.enova.Listener.ListenerTopic;
import tn.enova.Models.Commons.ConnectionInfo;
import tn.enova.Models.Commons.Publish;
import tn.enova.Models.Commons.Robot;
import tn.enova.Services.MQTTService;
import tn.enova.Services.ObjectMapperService;
import tn.enova.Services.RobotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class PropertyRobotTopicController {
    private final RobotService robotService;
    private final ObjectMapperService objMapperService;
    private final MQTTService mqttService;

    @ListenerTopic(topic = "$SYS/brokers/+/clients/#", qos = 0)
    public void getStatusConnectionClient(String topic, byte[] message) throws JsonProcessingException, MqttException, RessourceNotFoundException {
        ConnectionInfo connectionInfo = objMapperService.fromJson(new String(message), ConnectionInfo.class);
        final Robot rDB = robotService.updateRobotConnection(connectionInfo);
        Map<String, Connection> connectionMap = new HashMap<>() {{put("value", rDB.getConnection());}};
        String connectionMapJson = objMapperService.toJson(connectionMap);
        Publish publish = Publish.childBuilder()
                .topic("topic/data/robot/" + rDB.getName() + "/property/CONNECTION")
                .retained(false)
                .qos(0)
                .payload(connectionMapJson.getBytes()).build();
        mqttService.publish(publish);
    }
    @ListenerTopic(topic = "topic/data/robot/+/property/STATUS_ROBOT", qos = 0)
    public void getPropertyStatusByName(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Status Robot format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        this.robotService.updateRobotStatus(parts[3],messageMap.get("value"));

    }

    @ListenerTopic(topic = "topic/data/robot/+/property/MODE_ROBOT", qos = 0)
    public void getPropertyModeByName(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Mode Robot format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        this.robotService.updateRobotMode(parts[3],messageMap.get("value"));
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/OPERATION_STATUS", qos = 0)
    public void getPropertyOperationStatusByName(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Operation Status format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        this.robotService.updateRobotOperationStatus(parts[3],messageMap.get("value"));
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/LEVEL_BATTERY", qos = 0)
    public void getPropertyLevelBatteryByName(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Level Battery format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        this.robotService.updateRobotLevelBattery(parts[3], messageMap.get("value"));
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/SPEED", qos = 0)
    public void getPropertySpeedByName(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Speed format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        this.robotService.updateRobotSpeed(parts[3], messageMap.get("value"));
    }



    @ListenerTopic(topic = "topic/data/robot/+/property/TAGCODE", qos = 0)
    public void getPropertyTagCodeByName(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Tag Code format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        this.robotService.updateRobotTagCode(parts[3], messageMap.get("value"));
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/DISTANCE", qos = 0)
    public void getPropertyDistanceByName(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Distance format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);
        this.robotService.updateRobotDistance(parts[3], messageMap.get("value"));
    }
}
