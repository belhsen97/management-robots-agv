package tn.enova.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.paho.client.mqttv3.MqttException;
import tn.enova.Models.Responses.NotificationResponse;

public interface NotificationService {
     void notify(NotificationResponse notificationResponse );
}
