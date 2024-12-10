package tn.enova.Services;

import tn.enova.Enums.TypeProperty;
import tn.enova.Models.Commons.RobotSetting;

import java.util.List;

public interface RobotSettingService extends IGenericCRUD<RobotSetting, String> {
    List<RobotSetting> update(List<RobotSetting> objects);
    List<RobotSetting> selectByTypeProperty(TypeProperty t);
    Double selectSpeedAverage();
}
