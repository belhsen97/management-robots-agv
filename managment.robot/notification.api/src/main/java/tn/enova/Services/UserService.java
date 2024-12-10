package tn.enova.Services;

import tn.enova.Models.Requests.UserRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<UserRequest> getUserByUsername(String username);
    CompletableFuture<?> getAllUsersbyIds(List<String> ids);
}
