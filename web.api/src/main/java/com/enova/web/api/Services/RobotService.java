package com.enova.web.api.Services;

import com.enova.web.api.Models.Entitys.RobotProperty;
import com.enova.web.api.Models.Responses.RobotData;
import com.enova.web.api.Models.Entitys.Robot;

import java.util.List;

public interface RobotService extends IGenericCRUD<Robot,String> {

    List<RobotProperty> selectAllDataPropertys( );
    List<RobotProperty>  selectAllDataPropertysByName(String name);
}
