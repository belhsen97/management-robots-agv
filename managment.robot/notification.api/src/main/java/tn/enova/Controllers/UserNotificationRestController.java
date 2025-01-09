package tn.enova.Controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.paho.client.mqttv3.MqttException;
import tn.enova.Enums.ReponseStatus;
import tn.enova.Enums.Roles;
import tn.enova.Exceptions.NotificationException;
import tn.enova.Mappers.NotificationMapper;
import tn.enova.Models.Commons.Publish;
import tn.enova.Models.Entitys.Notification;
import tn.enova.Models.Requests.NotificationUser;
import tn.enova.Models.Responses.MsgReponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Services.MQTTService;
import tn.enova.Services.NotificationService;
import tn.enova.Services.ObjectMapperService;
import tn.enova.Services.UserService;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserNotificationRestController {
    private final NotificationService notificationService;
    private final UserService userService;
    private final NotificationMapper notificationMapper;
    private final MQTTService mqttService;
    private final ObjectMapperService objMapperService;

    @PostMapping("/notify")
    public   ResponseEntity<?> notifyUser(@RequestBody NotificationUser notificationUser)  {
        userService.getUserByUsername(notificationUser.getFrom())
                .thenAccept(userRequest -> {
                    System.out.println(notificationUser.toString());
                    Notification notification =  notificationMapper.mapNotificationUserToEntity(notificationUser,userRequest);
                    notificationService.insert(notification);
                    System.out.println(notification.toString());
                    NotificationResponse notificationResponse =  notificationMapper.mapNotificationUserToResponse(notificationUser,userRequest);
                    try {
                   String notificationResponseJson =  objMapperService.toJson(notificationResponse);
                    Publish publish =  Publish.childBuilder()
                            .topic("topic/data/service/notification/user/name/"+userRequest.getUsername())
                            .retained(false)
                            .qos(0)
                            .payload(notificationResponseJson.getBytes())
                            .build();
                        mqttService.publish(publish);
                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }


                })
                .exceptionally(ex -> {
                    System.err.println("Failed to fetch user by username  : " + ex.getMessage());
                    return null;
                });
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Notification User")
                .message("successfull send user")
                .datestamp(new Date()).build());
    }

    @GetMapping()
    public CompletableFuture<List<NotificationResponse>> GetAll(@RequestHeader("auth-user-id") String authUsername) {
        System.out.println(authUsername);
        return userService.getUserByUsername(authUsername)
                .thenCompose(userRequest -> {
                    System.out.println(userRequest.toString());
                    List<Notification> listNotification = notificationService.selectAllByRole(userRequest);
                    return notificationMapper.mapToListResponse(listNotification);
                })
                .exceptionally(ex -> {
                    throw new NotificationException("Failed to fetch user by username or process notifications", ex);
                });
    }
}