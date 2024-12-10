package tn.enova.Services;


import tn.enova.Models.Commons.notification.Notification;

import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    CompletableFuture<?> send(Notification payload);
}
