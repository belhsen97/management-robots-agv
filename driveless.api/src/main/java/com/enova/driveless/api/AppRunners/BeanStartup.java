package com.enova.driveless.api.AppRunners;

import com.enova.driveless.api.Models.Commons.Publish;
import com.enova.driveless.api.Services.MQTTService;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Order(value = 1)
@Component
@RequiredArgsConstructor
public class BeanStartup implements CommandLineRunner {
    private final MQTTService mqttService;
    @Override
    public void run(String... args) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Publish publish =  Publish.childBuilder().topic("topic/control/robot/"+"robot-1"+"/response-last-update").retained(false).qos(0).payload("robotJson55".getBytes()).build();
        try {
            mqttService.publish(publish);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}