package com.enova.driveless.api.Callbacks;



import com.enova.driveless.api.Exceptions.RessourceNotFoundException;
import com.enova.driveless.api.Models.Entitys.Robot;
import com.enova.driveless.api.Services.ObjectMapperService;
import com.enova.driveless.api.Services.RobotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CollectorCalback implements MqttCallback {

    private final ObjectMapperService objMapperService;
    private final RobotService robotService;

    // private final TopicListenerManager listenerManager;
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("connectionLost: " + cause.getMessage());
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) {
       // System.out.println("topic: " + topic);
       // System.out.println("Qos: " + message.getQos());
       // System.out.println("message content: " + new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}
