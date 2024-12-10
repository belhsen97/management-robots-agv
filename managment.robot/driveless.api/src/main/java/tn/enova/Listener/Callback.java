package tn.enova.Listener;

import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class Callback implements MqttCallback {

    private final TopicListenerManager listenerManager;
    private final MqttClient client;
    @PostConstruct
    public void onCreate() throws MqttException {
        System.out.println("Connection Mqtt Initialized");
         client.setCallback(this);
    }
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("connectionLost: " + cause.getMessage());
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) {
        listenerManager.handleMessageArrived(topic, message.getPayload());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}
