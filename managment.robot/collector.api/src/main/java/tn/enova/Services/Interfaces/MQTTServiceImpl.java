package tn.enova.Services.Interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;
import tn.enova.Models.Commons.Publish;
import tn.enova.Models.Commons.Subscribe;
import tn.enova.Services.MQTTService;
import tn.enova.Services.ObjectMapperService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;

@Service("mqtt-service")
@RequiredArgsConstructor
public class MQTTServiceImpl implements MQTTService {

    private final ObjectMapperService objectMapperService;
    private final MqttClient client;
    private final MqttConnectOptions mqttConnectOptions;
    private final ExecutorService executorService;

    @PostConstruct
    public void onCreate() throws MqttException {
        if (!client.isConnected()) {
            client.connect(mqttConnectOptions);
        }
    }

    @Override
    public void subscribe(Subscribe sub) throws MqttException {
        client.subscribe(sub.getTopic(), sub.getQos());
    }

    @Override
    public void publish(Publish pub) throws MqttException {
        if (!client.isConnected()) {
            System.out.println("Client is not connected, attempting to reconnect...");
            client.connect(mqttConnectOptions);
        }
        System.out.println("Publishing to topic: " + pub.getTopic());
        executorService.submit(() -> {
            try {
                client.publish(pub.getTopic(), pub.getPayload(), pub.getQos(), pub.isRetained());
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }

        });
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