package com.enova.notification.api.Repositorys;

import com.enova.notification.api.Models.Entitys.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
}
