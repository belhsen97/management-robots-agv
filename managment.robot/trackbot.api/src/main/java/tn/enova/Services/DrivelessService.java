package tn.enova.Services;

import tn.enova.Models.Entitys.Robot;
import tn.enova.Models.Entitys.RobotSetting;
import tn.enova.Models.Entitys.Tag;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DrivelessService {
    CompletableFuture<?> insertRobot (Robot object);
    CompletableFuture<?> updateRobot ( String id , Robot object);
    CompletableFuture<?> deleteRobot(String id );
    CompletableFuture<?> deleteAllRobots( );

    CompletableFuture<?> insertTag (Tag object);
    CompletableFuture<?> updateTag ( String id , Tag object);
    CompletableFuture<?> deleteTag (String id );
    CompletableFuture<?> deleteAllTags( );


    CompletableFuture<?> insertRobotSetting (RobotSetting object);
    CompletableFuture<?> updateRobotSetting ( String id , RobotSetting object);
    CompletableFuture<?> updateMultiRobotSetting(List<RobotSetting> list);
    CompletableFuture<?> deleteRobotSetting(String id );
    CompletableFuture<?> deleteAllRobotSettings();
}
