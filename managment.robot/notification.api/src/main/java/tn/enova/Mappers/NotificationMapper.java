package tn.enova.Mappers;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.enova.Configures.ParameterConfig;
import tn.enova.Enums.LevelType;
import tn.enova.Enums.TypeSender;
import tn.enova.Exceptions.NotificationNullPointerException;
import tn.enova.Models.Entitys.Notification;
import tn.enova.Models.Requests.NotificationRobot;
import tn.enova.Models.Requests.NotificationUser;
import tn.enova.Models.Requests.RobotRequest;
import tn.enova.Models.Requests.UserRequest;
import tn.enova.Models.Responses.NotificationResponse;
import tn.enova.Services.TrackBotService;
import tn.enova.Services.UserService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class NotificationMapper {
    private final ParameterConfig parameterConfig;
    private final UserService userService;
    private final TrackBotService trackBotService;

    public  Notification  mapNotificationUserToEntity(NotificationUser notificationUser ,UserRequest userRequest) {
        if ( userRequest == null ){    throw new NotificationNullPointerException("UserRequest is null");}
        if ( userRequest.getId() == null || userRequest.getId().equals("") ){    throw new NotificationNullPointerException("UserRequest property id is null or empty");}
        if (notificationUser.getLevel() == null ){notificationUser.setLevel(LevelType.INFO);}
        return Notification.builder().sender(userRequest.getId())
                .typeSender(TypeSender.USER)
                .createdAt( ( notificationUser.getTimestamp() == null ?  new Date().getTime(): notificationUser.getTimestamp() ))
                .level(notificationUser.getLevel())
                .message(notificationUser.getMessage())
                .displayType("WEB")
                .build();
    }
    public  NotificationResponse  mapNotificationUserToResponse(NotificationUser notificationUser,UserRequest userRequest) {
        String completenameUser = (( ((userRequest.getFirstname() == null ||userRequest.getFirstname().equals("") ) || (userRequest.getLastname() == null||userRequest.getLastname().equals("")))  ? userRequest.getUsername() : userRequest.getLastname() + " " + userRequest.getFirstname()));
        if (notificationUser.getLevel() == null ){notificationUser.setLevel(LevelType.INFO);}
        return NotificationResponse.builder().senderName(completenameUser)
                .senderImageUrl(userRequest.getPhoto() != null ? userRequest.getPhoto().getDownloadURL() : "")
                .createdAt(notificationUser.getTimestamp())
                .level(notificationUser.getLevel())
                .message(notificationUser.getMessage())
                .build();
    }

    public  Notification  mapNotificationRobotToEntity(NotificationRobot notification) {
        if ( notification.getFrom() == null ){    throw new NotificationNullPointerException("NotificationRobot property from  is null");}
        if ( notification.getFrom().getId() == null || notification.getFrom().getId().equals("") ){    throw new NotificationNullPointerException("NotificationRobot property id is null or empty");}
        if (notification.getLevel() == null ){notification.setLevel(LevelType.INFO);}
        return Notification.builder().sender(notification.getFrom().getId())
                .typeSender(TypeSender.ROBOT)
                .createdAt( ( notification.getTimestamp() == null ?  new Date().getTime(): notification.getTimestamp() ))
                .level(notification.getLevel())
                .message(notification.getMessage())
                .displayType("WEB")
                .build();
    }
    public  NotificationResponse  mapNotificationRobotToResponse(NotificationRobot notification) {
      return NotificationResponse.builder().senderName((notification.getFrom() == null ? "Unknown" : (notification.getFrom().getName() == null || notification.getFrom().getName().equals("")) ? "Unknown"  :notification.getFrom().getName()))
                                               .senderImageUrl(parameterConfig.linkRobotPhoto)
                                               .createdAt(notification.getTimestamp())
                                               .level(notification.getLevel())
                                               .message(notification.getMessage())
                                               .build();
    }
    public  CompletableFuture<List<NotificationResponse>>  mapToListResponse(List<Notification> notificationList) {
        List<String> idsRobot = this.getRobotSenders(notificationList,TypeSender.ROBOT);
        List<String> idsUsers = this.getRobotSenders(notificationList,TypeSender.USER);
        AtomicReference<List<RobotRequest>> robotResults = new AtomicReference<>(new ArrayList<>());
        AtomicReference<List<UserRequest>> userResults = new AtomicReference<>(new ArrayList<>());
        CompletableFuture<Void> fetchRobots =   this.trackBotService.getAllRobotsbyIds(idsRobot)
                .thenAccept(result -> {
                    List<RobotRequest> robots = (List<RobotRequest>) result;
                    robotResults.set(robots);})
                .exceptionally(ex -> {
                    System.err.println("Failed to fetch robots : " + ex.getMessage());
                    return null;
                });
        CompletableFuture<Void> fetchUsers =   this.userService.getAllUsersbyIds(idsUsers)
                .thenAccept(result -> {
                    List<UserRequest> users = (List<UserRequest>) result;
                    userResults.set(users);})
                .exceptionally(ex -> {
                    System.err.println("Failed to fetch robots : " + ex.getMessage());
                    return null;
                });
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(fetchRobots, fetchUsers);
        // Return a CompletableFuture that combines the results
        return allTasks.thenApplyAsync(v -> {
            // Wait for both tasks to finish
            allTasks.join();

            // Get results after both tasks complete
            List<RobotRequest> robots = robotResults.get();
            List<UserRequest> users = userResults.get();
            List<NotificationResponse> robotResponses = maprROBOTToResponse(notificationList, robots);
            List<NotificationResponse> userResponses = mapUSERToResponse(notificationList, users);
            List<NotificationResponse> mergedResponses = new ArrayList<>();
            mergedResponses.addAll(robotResponses);
            mergedResponses.addAll(userResponses);

            return mergedResponses;
        });
    }

    public List<NotificationResponse> maprROBOTToResponse(List<Notification> notificationList, List<RobotRequest> robots) {
        if ( robots == null ){return new ArrayList<>();}
        // Create a map of Robot id to Robot for easy lookup
        Map<String, RobotRequest> robotMap = robots.stream()
                .collect(Collectors.toMap(RobotRequest::getId, robot -> robot));

        // Map notifications to responses
        return notificationList.stream()
                .filter(notification -> notification.getTypeSender() == TypeSender.ROBOT) // Filter only ROBOT type notifications
                .map(notification -> {
                    RobotRequest robot = robotMap.get(notification.getSender());
                    if (robot != null) {
                        return NotificationResponse.builder()
                                .id(notification.getId())
                                .senderName(robot.getName())
                                .senderImageUrl(parameterConfig.linkRobotPhoto)
                                .createdAt(notification.getCreatedAt())
                                .level(notification.getLevel())
                                .message(notification.getMessage())
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull) // Filter out any null results (if no match found)
                .collect(Collectors.toList());
    }


    public List<NotificationResponse> mapUSERToResponse(List<Notification> notificationList, List<UserRequest> userList) {
        if ( userList == null ){return new ArrayList<>();}
        // Create a map of User id to UserRequest for easy lookup
        Map<String, UserRequest> userMap = userList.stream()
                .collect(Collectors.toMap(UserRequest::getId, user -> user));

        // Map notifications to responses
        return notificationList.stream()
                .filter(notification -> notification.getTypeSender() == TypeSender.USER) // Filter only USER type notifications
                .map(notification -> {
                    // Find corresponding UserRequest by sender id
                    UserRequest user = userMap.get(notification.getSender());
                    if (user != null) {
                        String completenameUser = (( ((user.getFirstname() == null ||user.getFirstname().equals("") ) || (user.getLastname() == null||user.getLastname().equals("")))  ? user.getUsername() : user.getLastname() + " " + user.getFirstname()));
                        return NotificationResponse.builder()
                                .id(notification.getId())
                                .senderName(completenameUser) // Concatenate lastname and firstname
                                .senderImageUrl(user.getPhoto() != null ? user.getPhoto().getDownloadURL() : "") // Use downloadURL for photo
                                .createdAt(notification.getCreatedAt())
                                .level(notification.getLevel())
                                .message(notification.getMessage())
                                .build();
                    }
                    return null; // Return null if no matching user is found (handle appropriately)
                })
                .filter(Objects::nonNull) // Filter out any null results (if no match found)
                .collect(Collectors.toList());
    }





    public List<String> getRobotSenders(List<Notification> notifications,TypeSender type) {
        return notifications.stream()
                .filter(notification -> notification.getTypeSender() == type) // Filter by TypeSender.ROBOT
                .map(Notification::getSender) // Extract the sender field
                .collect(Collectors.toList()); // Collect to a list
    }
}
  /* allTasks.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("All tasks finished successfully.");
                // Successfully completed both tasks
                List<Robot> robots = robotResults.get();
                List<UserRequest> users = userResults.get();

                // Print the results
                System.out.println("Robots: " + robots);
                System.out.println("Users: " + users);
            } else {
                System.err.println("An error occurred while fetching data.");
                ex.printStackTrace(); // Print the exception details
            }
        });
        allTasks.join();
        return new ArrayList<>();*/