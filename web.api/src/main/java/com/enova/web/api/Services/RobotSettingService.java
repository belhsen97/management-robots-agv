package com.enova.web.api.Services;

import com.enova.web.api.Models.Entitys.RobotSetting;

import java.util.List;

public interface RobotSettingService extends IGenericCRUD<RobotSetting, String> {
    List<RobotSetting> update(List<RobotSetting> objects);
}
