package com.enova.web.api.Services;

import com.enova.web.api.Models.Entitys.RobotProperty;
import com.enova.web.api.Models.Responses.RobotDataChart;
import com.enova.web.api.Models.Entitys.Robot;

import java.util.Date;
import java.util.List;

public interface RobotService extends IGenericCRUD<Robot,String> {

    Robot selectByName(String name);
    List<RobotProperty> selectDataPropertysAllOrByNameOrUnixTimestamps(String name, Long start, Long end);

}
