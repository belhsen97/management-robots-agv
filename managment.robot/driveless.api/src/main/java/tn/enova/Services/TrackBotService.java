package tn.enova.Services;

import java.util.concurrent.CompletableFuture;

public interface TrackBotService {
    CompletableFuture<?> getAllRobots();
    CompletableFuture<?> getAllTags();
    CompletableFuture<?> getAllRobotSettings();
}
