package com.enova.collector.api.Services;



import com.enova.collector.api.Enums.Connection;
import com.enova.collector.api.Models.Entitys.Robot;
import com.enova.collector.api.Models.Entitys.RobotProperty;

import java.util.List;

public interface RobotService  {
    Robot selectByName( String name);
    Robot selectByClientId( String clientId);
    void insertDataPropertys(List<RobotProperty> listPropertys);
    void updateRobot( Robot r, Robot ru);
    void updateRobotConnection( String clientId ,  Connection c);
}
