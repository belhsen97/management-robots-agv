package tn.enova.AppRunners;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tn.enova.Services.NotificationService;
import tn.enova.Services.TaskSchedulingService;
import tn.enova.Services.UserService;

import javax.annotation.PreDestroy;
import java.util.List;


@Order(value = 1)
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BeanStartup implements CommandLineRunner {
    private final NotificationService notificationService;
    private final TaskSchedulingService taskSchedulingService;
    String idScheduleTask = null;
    @Override
    public void run(String... args) {
       idScheduleTask =   taskSchedulingService.addScheduleTask(new Runnable() {
            @Override
            public void run() {
                Long currentTimestamp = System.currentTimeMillis();
                notificationService.deleteExpiredNotificationsAtBefore(currentTimestamp);
            }
        }, "0 0 0 * * *");
    }
    @PreDestroy
    public void onDestroy()  {
        System.out.println("BeanStartup onDestroy");
        if (idScheduleTask != null ) {
            taskSchedulingService.removeScheduledTask(idScheduleTask);
        }
    }
}