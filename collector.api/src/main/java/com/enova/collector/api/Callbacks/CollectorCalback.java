package com.enova.collector.api.Callbacks;


import com.enova.collector.api.Models.Entitys.Robot;
import com.enova.collector.api.Exceptions.RessourceNotFoundException;
import com.enova.collector.api.Services.ObjectMapperService;
import com.enova.collector.api.Services.RobotService;
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

    @Async
    @Override
    public void messageArrived(String topic, MqttMessage message) {
//        System.out.println("topic: " + topic);
//        System.out.println("Qos: " + message.getQos());
        //System.out.println("message content: " + new String(message.getPayload()));
        //listenerManager.handleMessage(topic, message.getPayload());
        // System.out.println("Async task started "+topic);
        try {
            final Robot r = objMapperService.fromJson(new String(message.getPayload()), Robot.class);
//            System.out.println(r);
            final Robot rDB = robotService.selectByName(r.getName());
            robotService.updateRobot(rDB, r);
            robotService.insertDataPropertys(r);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); //throw new com.enova.collector.api.Exceptions.JsonProcessingException(e.getMessage());
        } catch (RessourceNotFoundException e) {
            e.printStackTrace();
        }
        //System.out.println("Async task completed"+ topic);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}
