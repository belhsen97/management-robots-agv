package tn.enova.Services;

import tn.enova.Models.Entitys.Robot;
import tn.enova.Models.Entitys.RobotProperty;

import java.util.List;

public interface RobotService extends IGenericCRUD<Robot,String> {
    List<Robot> selectAllByIds(List<String> ids);
    Robot selectByName(String name);
    List<RobotProperty> selectDataPropertysAllOrByNameOrUnixTimestamps(String name, Long start, Long end);

}
