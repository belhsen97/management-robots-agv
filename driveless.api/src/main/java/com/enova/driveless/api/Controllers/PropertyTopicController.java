package com.enova.driveless.api.Controllers;

import com.enova.driveless.api.Enums.Connection;
import com.enova.driveless.api.Exceptions.RessourceNotFoundException;
import com.enova.driveless.api.Listener.ListenerTopic;
import com.enova.driveless.api.Models.Commons.ConnectionInfo;
import com.enova.driveless.api.Models.Commons.Publish;
import com.enova.driveless.api.Models.Entitys.Robot;
import com.enova.driveless.api.Services.MQTTService;
import com.enova.driveless.api.Services.ObjectMapperService;
import com.enova.driveless.api.Services.RobotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class PropertyTopicController {
    private final RobotService robotService;
    private final ObjectMapperService objMapperService;
    private final MQTTService mqttService;


    @ListenerTopic(topic = "$SYS/brokers/+/clients/#", qos = 0)
    public void getStatusConnectionClient(String topic, byte[] message) throws JsonProcessingException, MqttException, RessourceNotFoundException {
        ConnectionInfo connectionInfo = objMapperService.fromJson(new String(message), ConnectionInfo.class);
        Connection stateConnection;
        if (connectionInfo.getConnectedAt() >= connectionInfo.getDisconnectedAt()) {
            stateConnection = Connection.CONNECTED;
        } else {
            stateConnection = Connection.DISCONNECTED;
        }
        final Robot rDB = robotService.updateRobotConnection(connectionInfo.getClientId(), stateConnection);
        Map<String, Connection> connectionMap = new HashMap<>() {{
            put("value", stateConnection);
        }};
        String connectionMapJson = objMapperService.toJson(connectionMap);
        Publish publish = Publish.childBuilder()
                .topic("topic/data/robot/" + rDB.getName() + "/property/CONNECTION")
                .retained(false)
                .qos(0)
                .payload(connectionMapJson.getBytes()).build();
        mqttService.publish(publish);
    }

    @ListenerTopic(topic = "topic/data/robot/+/property/STATUS_ROBOT", qos = 0)
    public void getPropertyStatus(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Status Robot format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);

    }

    @ListenerTopic(topic = "topic/data/robot/+/property/MODE_ROBOT", qos = 0)
    public void getPropertyMode(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Mode Robot format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);

    }

    @ListenerTopic(topic = "topic/data/robot/+/property/OPERATION_STATUS", qos = 0)
    public void getPropertyOperationStatus(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Operation Status format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);

    }

    @ListenerTopic(topic = "topic/data/robot/+/property/LEVEL_BATTERY", qos = 0)
    public void getPropertyLevelBattery(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Level Battery format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);

    }

    @ListenerTopic(topic = "topic/data/robot/+/property/SPEED", qos = 0)
    public void getPropertySpeed(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Speed format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);

    }

    @ListenerTopic(topic = "topic/data/robot/+/property/DISTANCE", qos = 0)
    public void getPropertyDistance(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Distance format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);

    }

    @ListenerTopic(topic = "topic/data/robot/+/property/TAGCODE", qos = 0)
    public void getPropertyTagCode(String topic, byte[] message) throws JsonProcessingException, RessourceNotFoundException {
        String[] parts = topic.split("/");
        int lengthParts = parts.length;
        if (lengthParts != 6) {
            throw new IllegalArgumentException("Invalid topic Tag Code format: " + topic);
        }
        final Map<String, Object> messageMap = objMapperService.fromJson(new String(message), Map.class);

    }
}
