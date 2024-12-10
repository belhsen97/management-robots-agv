package tn.enova.Services.Interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;
import tn.enova.Enums.TypeProperty;
import tn.enova.Models.Commons.Publish;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Services.MQTTService;
import tn.enova.Services.NotificationService;
import tn.enova.Services.ObjectMapperService;
import tn.enova.Services.OrderService;

import java.util.HashMap;
import java.util.Map;


@Service("order-service")
@RequiredArgsConstructor
public class OrderServiceImpl  implements OrderService {
        private final MQTTService mqttService;
        private final ObjectMapperService objMapperService;
        @Override
        public void send(TypeProperty type , Object value ) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", value);
            try {
                String notificationMapJson = objMapperService.toJson(map);
                Publish publish = Publish.childBuilder()
                        .topic("topic/control/robot/all/property/"+type.name())
                        .retained(false)
                        .qos(0)
                        .payload(notificationMapJson.getBytes()).build();
                mqttService.publish(publish);
            }
            catch (MqttException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

