package com.enova.web.api.Services;

import com.enova.web.api.Models.Entitys.RobotProperty;
import com.enova.web.api.Models.Responses.RobotDataChart;
import com.enova.web.api.Models.Entitys.Robot;

import java.util.Date;
import java.util.List;

public interface RobotService extends IGenericCRUD<Robot,String> {

    List<RobotProperty> selectAllDataPropertys( );
    List<RobotProperty> selectDataPropertysByTimestampBetween(Date start, Date end);

    List<RobotProperty>  selectAllDataPropertysByName(String name);
    List<RobotProperty> selectDataPropertysByNameAndDateTimes(String name, Date start, Date end);

    List<RobotProperty> selectDataPropertysByNameAndUnixTimestamps(String name, Long start, Long end);

}
