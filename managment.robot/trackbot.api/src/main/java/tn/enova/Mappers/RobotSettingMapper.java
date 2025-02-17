package tn.enova.Mappers;


import tn.enova.Enums.Constraint;
import tn.enova.Enums.TypeProperty;
import tn.enova.Models.Dtos.RobotSettingDto;
import tn.enova.Models.Entitys.RobotSetting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
