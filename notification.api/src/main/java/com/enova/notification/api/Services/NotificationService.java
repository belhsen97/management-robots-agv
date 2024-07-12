package com.enova.notification.api.Services;



import com.enova.notification.api.Models.Entitys.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> selectAll ();
    Notification  selectById (String id);
    Notification insert( Notification notification);
    Notification update( String id , Notification notification);
    void delete(  String id );
}
