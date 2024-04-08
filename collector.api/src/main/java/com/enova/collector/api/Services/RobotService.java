package com.enova.collector.api.Services;



import com.enova.collector.api.Models.Entitys.Robot;
import com.enova.collector.api.Models.Entitys.RobotProperty;

import java.util.List;

public interface RobotService  {
    public Robot selectByName( String name);
    void insertDataPropertys( Robot r);
    void updateRobot( Robot r, Robot ru);
}
