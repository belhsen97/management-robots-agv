package com.enova.collector.api.Services.Interfaces;

import com.enova.collector.api.Callbacks.CollectorCalback;
import com.enova.collector.api.Services.MQTTService;
import com.enova.collector.api.Services.ObjectMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service("mqtt-service")
@RequiredArgsConstructor
public class MQTTServiceImpl implements MQTTService {
    private final int qos = 0;//https://www.hivemq.com/blog/mqtt-essentials-part-6-mqtt-quality-of-service-levels/
    private final ObjectMapperService objectMapperService;

    private final MqttClient client;


    private final MqttConnectOptions mqttConnectOptions;


    private final CollectorCalback collectorCalback;

    @PostConstruct
    public void onCreate() throws MqttException {
        System.out.println("UserService Initialized");
        if ( !client.isConnected()){
            client.connect(mqttConnectOptions);
            System.out.println("client is connect");
        }
    }
    @Override
    public void subscribe(String topic) throws MqttException {
        client.setCallback(collectorCalback);
        client.subscribe(topic, qos);//"testtopic/#"
    }
    @Override
    public void publish(String topic, String message) throws MqttException {
        client.publish(topic, message.getBytes(), qos, false);
    }

    @Override
    public void publish(String topic, Object obj) throws MqttException, JsonProcessingException {
        final String content = objectMapperService.toJson(obj);
        client.publish(topic, content.getBytes(), qos, false);
    }

    @PreDestroy
    public void onDestroy() throws MqttException {
        System.out.println("MQTTService Destroyed");
        if ( client.isConnected()){
            client.disconnect();
            System.out.println("client is disconnect");
        }
    }
}



//        String content = "Hello MQTT";
//        MqttMessage message = new MqttMessage(content.getBytes());
//        message.setQos(0);
//        client.publish("testtopic/1", message);