package tn.enova.Repositorys;

import org.springframework.data.mongodb.repository.Aggregation;
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
    List<Notification> findAllByOrderByCreatedAtDesc();

    @Aggregation(pipeline = {
            "{ '$match':{ '$and':[{ '$or': [{'typeSender':'USER','sender':?0},{'typeSender':'ROBOT'}]},{  '$or':[{'level':'INFO'},{'level':'WARNING'}]  }]}}",
            "{ '$sort': { 'createdAt': -1 } }"
    })
    List<Notification> findFilteredNotificationsByUserAndSenderRobotAndLevelInfoAndWarning(String idUser);
    @Aggregation(pipeline = {
            "{ '$match':{ '$and':[{ '$or': [{'typeSender':'USER','sender':?0},{'typeSender':'ROBOT'}]},{   '$or':[{'level':'INFO'},{'level':'WARNING'},{'level':'ERROR'},{'level':'DEBUG'}]   }]}}",
            "{ '$sort': { 'createdAt': -1 } }"
    })
    List<Notification> findFilteredNotificationsByUserAndSenderRobotWithoutLevelTrace(String idUser);
}
