package com.enova.collector.api.Services;



import com.enova.collector.api.Models.Entitys.Robot;
import com.enova.collector.api.Models.Entitys.RobotProperty;

import java.util.List;

public interface RobotService  {
    public Robot selectById( String id);
    public Robot selectByName( String name);
    public Robot update( String id, Robot obj);
    public Robot updatebyName( String name, Robot obj);
    void insertDataPropertys( Robot r);
    List<RobotProperty> selectDataPropertys( );
    void updateRobot( Robot r, Robot ru);
}
