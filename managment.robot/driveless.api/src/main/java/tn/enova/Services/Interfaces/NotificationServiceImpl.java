package tn.enova.Services.Interfaces;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;
import tn.enova.Models.Commons.Publish;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Services.MQTTService;
import tn.enova.Services.NotificationService;
import tn.enova.Services.ObjectMapperService;

@Service("notification-service")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final MQTTService mqttService;
    private final ObjectMapperService objMapperService;
    @Override
    public void notify(NotificationResponse notificationResponse ) {
        try {
            String notificationMapJson = objMapperService.toJson(notificationResponse);
            Publish publish = Publish.childBuilder()
                    .topic("topic/data/service/driveless/notification/robot/name/" + notificationResponse.getFrom().getName())
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
