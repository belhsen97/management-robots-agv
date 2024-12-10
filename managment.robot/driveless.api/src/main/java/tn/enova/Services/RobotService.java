package tn.enova.Services;



import tn.enova.Enums.Connection;
import tn.enova.Enums.ModeRobot;
import tn.enova.Enums.OperationStatus;
import tn.enova.Enums.StatusRobot;
import tn.enova.Models.Commons.ConnectionInfo;
import tn.enova.Models.Commons.Robot;

import java.util.Optional;

public interface RobotService  extends IGenericCRUD<Robot,String>{
    Optional<Robot> selectByUsernameAndPassword(String username, String password);
    Robot selectByClientId( String clientId);
    Robot selectByName(String name);


    Robot  updateRobotConnection(   ConnectionInfo connectionInfo );
    Robot  updateRobotStatus( String nameRobot ,  StatusRobot s);
    Robot  updateRobotMode( String nameRobot ,  ModeRobot m);
    Robot  updateRobotOperationStatus( String nameRobot ,  OperationStatus o);
    Robot  updateRobotLevelBattery( String nameRobot ,  double value);
    Robot  updateRobotSpeed( String nameRobot ,  double value);
    Robot  updateRobotTagCode( String nameRobot ,  String code);
    void   updateAllRobotsMode( ModeRobot newMode);
    void   updateAllRobotsSpeed( double value);
    void   updateAllRobotsOperationStatus( OperationStatus newOperationStatus);
    void   updateAllRobotsStatus( StatusRobot newStatus);
}
