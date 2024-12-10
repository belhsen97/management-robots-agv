package tn.enova.Services;


import tn.enova.Models.Responses.NotificationResponse;

import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    CompletableFuture<?> send(NotificationResponse payload);
    CompletableFuture<?> notify(NotificationResponse ntf);
}
