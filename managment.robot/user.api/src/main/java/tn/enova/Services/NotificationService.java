package tn.enova.Services;

import tn.enova.Models.Commons.mail.Msg;
import tn.enova.Models.Responses.NotificationResponse;

import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    CompletableFuture<?> send(NotificationResponse notify);
    CompletableFuture<?> send(Msg msg);
}
