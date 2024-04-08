package com.enova.driveless.api.Configures;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MQTTClientConfig {
    @Value("${mqtt.broker}")
    String broker = "tcp://broker.emqx.io:1883";
    @Value("${mqtt.topic}")
    public String topic = "testtopic/#";
    @Value("${mqtt.clean.session}")
    boolean cleanSession = true;
    @Value("${mqtt.connection.timeout}")
    int connectionTimeout = 60;
    @Value("${mqtt.keepalive.interval}")
    int keepAliveInterval = 60;

    @Value("${mqtt.clientid}")
    String clientid = "driveless-service-client";
    @Value("${mqtt.user.username}")
    String username = "test";
    @Value("${mqtt.user.password}")
    String password = "test";

    @Bean
    public MqttClient mqttClient() throws MqttException {
        String formattedDate = new SimpleDateFormat("HH-mm-ssSSS").format(new Date());
        clientid += "-" + formattedDate;
        return new MqttClient(broker, clientid, new MemoryPersistence());
    }

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(cleanSession);
        options.setUserName(this.username);
        options.setPassword(this.password.toCharArray());
        options.setConnectionTimeout(this.connectionTimeout);
        options.setKeepAliveInterval(this.keepAliveInterval);
        return options;
    }
}
