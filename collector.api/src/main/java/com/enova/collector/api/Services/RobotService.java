package com.enova.collector.api.Services;



import com.enova.collector.api.Models.Entitys.Robot;

public interface RobotService  {
    public Robot selectById( String id);
    public Robot selectByName( String name);
    public Robot update( String id, Robot obj);
    public Robot updatebyName( String name, Robot obj);
    void insertDataPropertys( Robot r);
    void updateRobot( Robot r, Robot ru);
}
