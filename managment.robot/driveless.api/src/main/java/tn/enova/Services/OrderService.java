package tn.enova.Services;

import tn.enova.Enums.TypeProperty;
import tn.enova.Models.Responses.NotificationResponse;

public interface OrderService {
     void send(TypeProperty type , Object value );
}
