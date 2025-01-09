package tn.enova.Services;



import tn.enova.Enums.Roles;
import tn.enova.Models.Entitys.Notification;
import tn.enova.Models.Requests.UserRequest;

import java.util.List;

public interface NotificationService {
    List<Notification> selectAll ();
    List<Notification> selectAllByRole(UserRequest user);
    Notification  selectById (String id);
    Notification insert( Notification notification);
    Notification update( String id , Notification notification);
    void delete(  String id );
    void deleteExpiredNotificationsAtBefore(Long currentTimestamp);
    List<Notification> selectByExpiredAtLessThan(Long date);
}
