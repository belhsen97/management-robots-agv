package tn.enova.Services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TrackBotService {
    CompletableFuture<?> getAllRobotsbyIds(List<String> ids);
}
