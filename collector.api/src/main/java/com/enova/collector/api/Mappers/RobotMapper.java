package com.enova.collector.api.Mappers;

import com.enova.collector.api.Models.Entitys.Robot;
import com.enova.collector.api.Models.Entitys.RobotProperty;
import com.enova.collector.api.Enums.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RobotMapper {
    public static List<RobotProperty> convertToRobotPropertyList(Robot robot) {
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
        if (robot.getCodeTag() != null) {
            robotProperties.add(RobotProperty.builder()
                    .id(robot.getId())
                    .name(robot.getName())
                    .timestamp(dateNow)
                    .type(TypeProperty.TAGCODE)
                    .value(robot.getCodeTag().toString())
                    .build());
        }
        robotProperties.add(RobotProperty.builder()
                .id(robot.getId())
                .name(robot.getName())
                .timestamp(dateNow)
                .type(TypeProperty.DISTANCE)
                .value(String.valueOf(robot.getLevelBattery()))
                .build());
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
    public static Robot mapRobotPropertysToRobot(Robot robot , List<RobotProperty> robotProperties) {
        if (robotProperties == null && robotProperties.isEmpty()) {
            return robot;
        }
        for (RobotProperty property : robotProperties) {
             mapRobotPropertyToRobot(robot , property);
        }
        return robot;
    }
    public static void mapRobotPropertyToRobot(Robot robot , RobotProperty property) {
        if (property == null) {throw new NullPointerException("property is null");}
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
                case DISTANCE:
                    robot.setDistance(Double.parseDouble(property.getValue()));
                case TAGCODE:
                    robot.setCodeTag(property.getValue());
            }
    }


}
