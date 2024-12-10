package tn.enova.Services;

import tn.enova.Models.Entitys.RobotSetting;

import java.util.List;

public interface RobotSettingService extends IGenericCRUD<RobotSetting, String> {
    List<RobotSetting> update(List<RobotSetting> objects);
}
