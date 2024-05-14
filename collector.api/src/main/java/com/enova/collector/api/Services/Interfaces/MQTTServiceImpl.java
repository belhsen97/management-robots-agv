package com.enova.collector.api.Services.Interfaces;

import com.enova.collector.api.Models.Commons.Publish;
import com.enova.collector.api.Models.Commons.Subscribe;
import com.enova.collector.api.Services.MQTTService;
import com.enova.collector.api.Services.ObjectMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service("mqtt-service")
@RequiredArgsConstructor
public class MQTTServiceImpl implements MQTTService {

    private final ObjectMapperService objectMapperService;
    private final MqttClient client;
    private final MqttConnectOptions mqttConnectOptions;
    // private final CollectorCalback collectorCalback;

    @PostConstruct
    public void onCreate() throws MqttException {
        System.out.println("UserService Initialized");
        if (!client.isConnected()) {
            client.connect(mqttConnectOptions);
            // client.setCallback(collectorCalback);
            System.out.println("client is connect");
        }
    }

    @Override
    public void subscribe(Subscribe sub) throws MqttException {
        client.subscribe(sub.getTopic(), sub.getQos());
    }

    @Override
    public void publish(Publish pub) throws MqttException {
        client.publish(pub.getTopic(), pub.getPayload(),  pub.getQos(), pub.isRetained());
        //client.publish(topic, message.getBytes(), qos, false);
    }

    @Override
    public void publish(String topic, int qos , boolean retained , Object obj) throws MqttException, JsonProcessingException {
        final String content = objectMapperService.toJson(obj);
        client.publish(topic, content.getBytes(), qos, retained);
    }

    @PreDestroy
    public void onDestroy() throws MqttException {
        System.out.println("MQTTService Destroyed");
        if (client.isConnected()) {
            client.disconnect();
            System.out.println("client is disconnect");
        }
    }
}

//        String content = "Hello MQTT";
//        MqttMessage message = new MqttMessage(content.getBytes());
//        message.setQos(0);
//        client.publish("testtopic/1", message);