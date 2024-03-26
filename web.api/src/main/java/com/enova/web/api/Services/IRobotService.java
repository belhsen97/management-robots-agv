package com.enova.web.api.Services;

import com.enova.web.api.Models.Responses.RobotData;
import com.enova.web.api.Models.Entitys.Robot;

import java.util.List;

public interface IRobotService extends IGenericCRUD<Robot,String> {
    List<RobotData> SelectAllDataById( String id);
}
