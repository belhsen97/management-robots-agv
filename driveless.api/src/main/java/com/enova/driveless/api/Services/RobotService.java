package com.enova.driveless.api.Services;


import com.enova.driveless.api.Enums.Connection;
import com.enova.driveless.api.Models.Entitys.Robot;
import com.enova.driveless.api.Models.Entitys.RobotSetting;

import java.util.List;
import java.util.Optional;

public interface RobotService  {
    Optional<Robot> selectByUsernameAndPassword(String username, String password);
    Robot selectByClientId( String clientId);
    Robot updateRobotConnection( String clientId ,  Connection c);
    Robot selectByName(String name);
    List<RobotSetting> selectAllRobotSetting();
}
