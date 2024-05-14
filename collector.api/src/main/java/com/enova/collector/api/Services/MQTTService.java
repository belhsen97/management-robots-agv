package com.enova.collector.api.Services;

import com.enova.collector.api.Models.Commons.Publish;
import com.enova.collector.api.Models.Commons.Subscribe;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface MQTTService {
    void subscribe(Subscribe sub) throws MqttException ;
    void publish(Publish pub)  throws MqttException;
    void publish(String topic, int qos , boolean retained , Object obj) throws MqttException, JsonProcessingException ;

}

