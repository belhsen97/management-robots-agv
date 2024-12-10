package tn.enova.Services.Interfaces;

import tn.enova.Enums.LevelType;
import tn.enova.Exceptions.NotificationNotFound;
import tn.enova.Exceptions.NotificationNullPointerException;
import tn.enova.Models.Commons.Publish;
import tn.enova.Models.Entitys.Notification;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Repositorys.NotificationRepository;
import tn.enova.Services.MQTTService;
import tn.enova.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enova.Services.ObjectMapperService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service("notificaation-service")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;

    @Override
    public List<Notification> selectAll() {
        return  this.repository.findAll();
    }

    @Override
    public Notification selectById(String id) {
        Optional<Notification> n = this.repository.findById(id);  // return this.userRepository.findById(id).orElseThrow();
        if (n.isEmpty()) {
            throw new NotificationNotFound("Cannot found Notification by id =" + id);
        }
        return n.get();
    }
    @Override
    public List<Notification> selectByExpiredAtLessThan(Long date) {
        return this.repository.findNotificationsByExpiredAtLessThan(date);
    }

    @Override
    public Notification insert(Notification notification) {
      notification.setExpiredAt(this.getExpireDate(notification.getLevel(), notification.getCreatedAt()));
        return this.repository.insert(notification);
    }


    private Long getExpireDate(LevelType type, Long startedDate) {
        // Convert the input timestamp to a ZonedDateTime
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(startedDate)
                .atZone(ZoneId.systemDefault());

        // Add the appropriate duration based on the LevelType
        switch (type) {
            case INFO:
                zonedDateTime = zonedDateTime.plus(7, ChronoUnit.DAYS); // Add 1 week
                break;
            case WARNING:
            case ERROR:
            case DEBUG:
                zonedDateTime = zonedDateTime.plus(14, ChronoUnit.DAYS); // Add 2 weeks
                break;
            case TRACE:
                zonedDateTime = zonedDateTime.plus(1, ChronoUnit.MONTHS); // Add 1 month
                break;
            default:
                throw new IllegalArgumentException("Unknown LevelType: " + type);
        }

        // Convert the ZonedDateTime back to milliseconds since epoch
        return zonedDateTime.toInstant().toEpochMilli();
    }

    @Override
    public Notification update(String id, Notification notification) {
        Notification  notificationdb =  this.selectById(id);
        notificationdb.setLevel(notification.getLevel());
        notificationdb.setMessage(notification.getMessage());
        return notificationdb;
    }

    @Override
    public void delete(String id) {
        Notification  notification =  this.selectById(id);
        this.repository.deleteById(id);
    }
    @Override
    public void deleteExpiredNotificationsAtBefore(Long currentTimestamp) {
        this.repository.deleteByExpiredAtLessThan(currentTimestamp);
    }

}
