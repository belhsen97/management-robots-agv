package com.enova.web.api.Mappers;


import com.enova.web.api.Enums.Constraint;
import com.enova.web.api.Enums.TypeProperty;
import com.enova.web.api.Models.Dtos.RobotSettingDto;
import com.enova.web.api.Models.Entitys.RobotSetting;

import java.util.*;

public class RobotSettingMapper {
    public static List<RobotSetting> mapToEntity(RobotSettingDto rdt) {
        return Arrays.asList(
                RobotSetting.builder().category(TypeProperty.DISTANCE).constraint(Constraint.MIN).value(rdt.getDistance().get("min").toString()).build(),
                RobotSetting.builder().category(TypeProperty.DISTANCE).constraint(Constraint.MAX).value(rdt.getDistance().get("max").toString()).build(),
                RobotSetting.builder().category(TypeProperty.SPEED).constraint(Constraint.MIN).value(rdt.getSpeed().get("min").toString()).build(),
                RobotSetting.builder().category(TypeProperty.SPEED).constraint(Constraint.MAX).value(rdt.getSpeed().get("max").toString()).build()
        );
    }

    public static RobotSettingDto mapToDto(List<RobotSetting> rs) {
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
        return RobotSettingDto.builder()
                .speed(speed)
                .distance(distance)
                .build();
    }
}
