package com.enova.collector.api.AppRunners;


import com.enova.collector.api.Configures.MQTTClientConfig;
import com.enova.collector.api.Services.MQTTService;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Order(value = 1)//Register beanStartup bean
@Component
@RequiredArgsConstructor
public class BeanStartup implements CommandLineRunner {
    private final MQTTService mqttService;
    private final MQTTClientConfig mqttClientConfig;
    @Override
    public void run(String... args) {
        try {
            mqttService.subscribe(mqttClientConfig.topic);
            //   mqttService2.publish("testtopic/1",robot);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


//    @ListenerTopic(topic = "robot/1")
//    public static void handleRobotTopicMessage(byte[]  message) {
//        System.out.println("Received message from handleRobotTopicMessage : " + new String(message));
//    }
//
//    @ListenerTopic(topic = "robot/2")
//    public static void handleErrorTopicMessage(byte[] message) {
//        System.out.println("Received message from handleErrorTopicMessage : " + new String(message));
//    }
}
//    private final MQTTService mqttService2;
//    private final ObjectMapperService objectMapperService;
//    private final RobotService robotService;
//    final Robot robot = Robot.builder()
//            //.id(i)
//            .idWorkstation("idWorkstation")
//            //.workstation(workstation1)
//            .name("robot-1")
//            .createdAt(new Date())
//            .connection(Connection.DISCONNECTED)
//            .modeRobot(ModeRobot.AUTO)
//            .statusRobot(StatusRobot.RUNNING)
//            .operationStatus(OperationStatus.PAUSE)
//            .levelBattery(100)
//            .speed(1.5)
//            .build();
//  System.out.println(mqttClientConfig.username);
//       System.out.println( objectMapperService.toJson(robot));
//String jsonRobot = "{\"id\":\"123\",\"name\"\"Robot1\"}";
//        System.out.println( objectMapperService.fromJson(jsonRobot, Robot.class));
////     throw new RuntimeException("Error converting object to JSON", e);             throw new RuntimeException("Error converting JSON to object", e);


//        catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

//  robotService.insertDataPropertys( robot );


//       final List<RobotProperty> list = RobotMapper.convertToRobotPropertyList(robot);
//        System.out.println( list);
//        System.out.println(  RobotMapper.convertToRobot(list)  );

//        try {
//        robotService.selectByName("robot");
//        }
//        catch (RessourceNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//        String jsonRobot = "{\"id\":\"123\",\"name\"\"Robot1\"}";
//            System.out.println( objectMapperService.fromJson(jsonRobot, Robot.class));
//        }
//        catch (JsonProcessingException e) {
//          throw
//        }


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