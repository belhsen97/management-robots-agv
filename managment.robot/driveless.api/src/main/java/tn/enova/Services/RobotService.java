package tn.enova.Services;



import tn.enova.Enums.ModeRobot;
import tn.enova.Enums.OperationStatus;
import tn.enova.Models.Commons.ConnectionInfo;
import tn.enova.Models.Commons.Robot;

import java.util.Optional;

public interface RobotService  extends IGenericCRUD<Robot,String>{
    Optional<Robot> selectByUsernameAndPassword(String username, String password);
    Robot selectByClientId( String clientId);
    Robot selectByName(String name);


    Robot  updateRobotConnection(   ConnectionInfo connectionInfo );
    Robot  updateRobotStatus( String name ,  Object value);
    Robot  updateRobotMode( String name  , Object value);
    Robot  updateRobotOperationStatus( String name , Object value);
    Robot  updateRobotLevelBattery( String name , Object value);
    Robot  updateRobotSpeed( String name ,  Object value);
    Robot  updateRobotTagCode( String name ,  Object value);
    Robot  updateRobotDistance( String name ,  Object value);


    void   updateAllRobotsMode( ModeRobot newMode);
    void   updateAllRobotsSpeed( double value);
    void   updateAllRobotsOperationStatus( OperationStatus newOperationStatus);
}
