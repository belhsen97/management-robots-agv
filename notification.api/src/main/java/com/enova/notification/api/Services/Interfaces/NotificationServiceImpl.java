package com.enova.notification.api.Services.Interfaces;

import com.enova.notification.api.Exceptions.NotificationNotFound;
import com.enova.notification.api.Models.Entitys.Notification;
import com.enova.notification.api.Repositorys.NotificationRepository;
import com.enova.notification.api.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Notification insert(Notification notification) {
        return this.repository.insert(notification);
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
}
