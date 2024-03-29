package com.enova.collector.api.Mappers;

import com.enova.collector.api.Models.Entitys.Robot;
import com.enova.collector.api.Models.Entitys.RobotProperty;
import com.enova.collector.api.Enums.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class RobotMapper {
    public List<RobotProperty> convertToRobotPropertyList(Robot robot) {
        List<RobotProperty> robotProperties = new ArrayList<>();
        if (robot == null) {
            return robotProperties;
        }
        final Date dateNow = new Date();
        if (robot.getConnection() != null) {
            robotProperties.add(RobotProperty.builder()
                    .id(robot.getId())
                    .name(robot.getName())
                    .timestamp(dateNow)
                    .type(TypeProperty.CONNECTION)
                    .value(robot.getConnection().toString())
                    .build());
        }
        if (robot.getStatusRobot() != null) {
            robotProperties.add(RobotProperty.builder()
                    .id(robot.getId())
                    .name(robot.getName())
                    .timestamp(dateNow)
                    .type(TypeProperty.STATUS_ROBOT)
                    .value(robot.getStatusRobot().toString())
                    .build());
        }
        if (robot.getModeRobot() != null) {
            robotProperties.add(RobotProperty.builder()
                    .id(robot.getId())
                    .name(robot.getName())
                    .timestamp(dateNow)
                    .type(TypeProperty.MODE_ROBOT)
                    .value(robot.getModeRobot().toString())
                    .build());
        }
        if (robot.getOperationStatus() != null) {
            robotProperties.add(RobotProperty.builder()
                    .id(robot.getId())
                    .name(robot.getName())
                    .timestamp(dateNow)
                    .type(TypeProperty.OPERATION_STATUS)
                    .value(robot.getOperationStatus().toString())
                    .build());
        }
        robotProperties.add(RobotProperty.builder()
                .id(robot.getId())
                .name(robot.getName())
                .timestamp(dateNow)
                .type(TypeProperty.LEVEL_BATTERY)
                .value(String.valueOf(robot.getLevelBattery()))
                .build());
        robotProperties.add(RobotProperty.builder()
                .id(robot.getId())
                .name(robot.getName())
                .timestamp(dateNow)
                .type(TypeProperty.SPEED)
                .value(String.valueOf(robot.getSpeed()))
                .build());
        return robotProperties;
    }

    public Robot convertToRobot(List<RobotProperty> robotProperties) {
        Robot robot = new Robot();
        if (robotProperties == null && robotProperties.isEmpty()) {
            return robot;
        }
        robot.setName(robotProperties.get(0).getName());
        for (RobotProperty property : robotProperties) {
            switch (property.getType()) {
                case CONNECTION:
                    robot.setConnection(Connection.valueOf(property.getValue()));
                    break;
                case STATUS_ROBOT:
                    robot.setStatusRobot(StatusRobot.valueOf(property.getValue()));
                    break;
                case MODE_ROBOT:
                    robot.setModeRobot(ModeRobot.valueOf(property.getValue()));
                    break;
                case OPERATION_STATUS:
                    robot.setOperationStatus(OperationStatus.valueOf(property.getValue()));
                    break;
                case LEVEL_BATTERY:
                    robot.setLevelBattery(Integer.parseInt(property.getValue()));
                    break;
                case SPEED:
                    robot.setSpeed(Double.parseDouble(property.getValue()));
                    break;
            }
        }
        return robot;
    }
}
