package com.enova.collector.api.appRunners;

import com.enova.collector.api.Entitys.Robot;
import com.enova.collector.api.Enums.Connection;
import com.enova.collector.api.Enums.ModeRobot;
import com.enova.collector.api.Enums.OperationStatus;
import com.enova.collector.api.Enums.StatusRobot;
import com.enova.collector.api.Services.MQTTService;
import com.enova.collector.api.Services.ObjectMapperService;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;


@Order(value = 1)//Register BeanRunnerOne bean
@Component
@RequiredArgsConstructor
public class SubscribeSample implements CommandLineRunner {
    private final MQTTService mqttService;
    private final MQTTService mqttService2;
    private final ObjectMapperService objectMapperService;


    final Robot robot = Robot.builder()
            //.id(i)
            .idWorkstation("idWorkstation")
            //.workstation(workstation1)
            .name("robot-1")
            .createdAt(new Date())
            .connection(Connection.CONNECTED)
            .modeRobot(ModeRobot.AUTO)
            .statusRobot(StatusRobot.RUNNING)
            .operationStatus(OperationStatus.PAUSE)
            .levelBattery(100)
            .speed(1.5)
            .build();


    @Override
    public void run(String... args) throws Exception {
      //  System.out.println(mqttClientConfig.username);
//       System.out.println( objectMapperService.toJson(robot));
//        String jsonRobot = "{\"id\":\"123\",\"name\"\"Robot1\"}";
//        System.out.println( objectMapperService.fromJson(jsonRobot, Robot.class));
////     throw new RuntimeException("Error converting object to JSON", e);             throw new RuntimeException("Error converting JSON to object", e);
        try {
            mqttService.subscribe("testtopic/#");

        } catch (MqttException e) {
            e.printStackTrace();
        }

//        String broker = "tcp://broker.emqx.io:1883";
//        String topic = "mqtt/test";
//        String username = "emqx";
//        String password = "public";
//        String clientid = "subscribe_client";

//        String broker = "tcp://localhost:1883";
//        String topic = "testtopic/#";
//        String username = "test";
//        String password = "test";
//        String clientid = "subscribe_client";
//        //String clientId = MqttClient.generateClientId();
//        int qos = 0;
//
//        try {
//            MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());
//            MqttConnectOptions options = new MqttConnectOptions();
//            options.setCleanSession(true);
//            options.setUserName(username);
//            options.setPassword(password.toCharArray());
//            options.setConnectionTimeout(60);
//            options.setKeepAliveInterval(60);
//
//            client.setCallback(new CollectorCalback());
//            client.connect(options);
//            client.subscribe(topic, qos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }



}
//            client.setCallback(new MqttCallback() {
//                public void connectionLost(Throwable cause) {
//                    System.out.println("connectionLost: " + cause.getMessage());
//                }
//                public void messageArrived(String topic, MqttMessage message) {
//                    System.out.println("topic: " + topic);
//                    System.out.println("Qos: " + message.getQos());
//                    System.out.println("message content: " + new String(message.getPayload()));
//                }
//                public void deliveryComplete(IMqttDeliveryToken token) {
//                    System.out.println("deliveryComplete---------" + token.isComplete());
//                }
//            });