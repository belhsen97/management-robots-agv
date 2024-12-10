package tn.enova.Services.Interfaces;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enova.Services.NotificationService;

@Service("traffic-service")
@RequiredArgsConstructor
public class TrafficServiceImpl {
    private final NotificationService notificationService;
}
