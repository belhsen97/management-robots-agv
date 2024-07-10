package com.enova.collector.api.Services;



import com.enova.collector.api.Enums.Connection;
import com.enova.collector.api.Models.Entitys.Robot;
import com.enova.collector.api.Models.Entitys.RobotProperty;

import java.util.List;

public interface RobotService  {
    Robot selectByName( String name);
    Robot selectByClientId( String clientId);
    void insertPropertyRobot(RobotProperty property);
    void insertPropertysRobot(List<RobotProperty> listPropertys);
    void updateRobotConnection( String clientId ,  Connection c);
    void  updateRobot( String name,  Robot robot);
    void updateConnection( String name , Object Value  );
    void updateStatusRobot( String name , Object Value  );
    void updateModeRobot( String name , Object Value  );
    void updateOperationStatus( String name , Object Value  );
    void updateLevelBattery( String name , Object Value  );
    void updateSpeed( String name , Object Value  );
    void updateDistance( String name , Object Value  );
    void updateTagCode( String name , Object Value  );
}
