package tn.enova.Repositorys;

import org.springframework.data.mongodb.repository.Query;
import tn.enova.Models.Entitys.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
   // @Query("{ 'expiredAt': { $lt: ?0 } }")
   // void deleteByExpiredAtBefore(Long now);
   void deleteByExpiredAtLessThan(Long now);
    List<Notification> findNotificationsByExpiredAtLessThan(Long timestamp);

}
