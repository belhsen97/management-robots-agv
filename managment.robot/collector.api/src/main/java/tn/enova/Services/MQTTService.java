package tn.enova.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.paho.client.mqttv3.MqttException;
import tn.enova.Models.Commons.Publish;
import tn.enova.Models.Commons.Subscribe;

public interface MQTTService {
    public void subscribe(Subscribe sub) throws MqttException ;
    public void publish(Publish pub)  throws MqttException;
    public void publish(String topic, int qos , boolean retained , Object obj) throws MqttException, JsonProcessingException ;

}
