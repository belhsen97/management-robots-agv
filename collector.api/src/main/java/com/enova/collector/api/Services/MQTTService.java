package com.enova.collector.api.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface MQTTService {
    public void subscribe(String topic) throws MqttException ;
    public void publish(String topic, String message) throws MqttException;
    public void publish(String topic, Object obj) throws MqttException, JsonProcessingException ;

}
