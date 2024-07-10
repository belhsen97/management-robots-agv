package com.enova.driveless.api.Mappers;

import com.enova.driveless.api.Models.Commons.RobotSettingCommon;
import com.enova.driveless.api.Models.Entitys.RobotSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotSettingMapper {
    public static RobotSettingCommon mapToDto(List<RobotSetting> rs) {
        Map<String, Double> speed = new HashMap<String, Double>();
        Map<String, Double> distance = new HashMap<String, Double>();
        for (RobotSetting r : rs) {
            switch (r.getCategory()) {
                case DISTANCE:
                    distance.put(r.getConstraint().name().toLowerCase(), Double.parseDouble(r.getValue()));
                    break;
                case SPEED:
                    speed.put(r.getConstraint().name().toLowerCase(), Double.parseDouble(r.getValue()));
                    break;
            }
        }
        return RobotSettingCommon.builder()
                .speed(speed)
                .distance(distance)
                .build();
    }
}
