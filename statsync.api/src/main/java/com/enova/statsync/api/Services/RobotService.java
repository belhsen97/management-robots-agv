package com.enova.statsync.api.Services;

import com.enova.statsync.api.Models.Entitys.Robot;
import com.enova.statsync.api.Models.Entitys.RobotProperty;
import java.util.List;

public interface RobotService  {
    public Robot selectById(String id);
    public Robot selectByName( String name);
    public Robot update( String id, Robot obj);
    public Robot updatebyName( String name, Robot obj);

    List<RobotProperty> selectAllDataPropertys( );
    void updateRobot( Robot r, Robot ru);

    List<RobotProperty>  selectAllDataPropertysByName(String id);
}
