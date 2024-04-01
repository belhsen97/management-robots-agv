package com.enova.statsync.api.Mappers;



import com.enova.statsync.api.Enums.*;
import com.enova.statsync.api.Models.Entitys.Robot;
import com.enova.statsync.api.Models.Entitys.RobotProperty;
import com.enova.statsync.api.Models.Responses.RobotData;

import java.util.*;
import java.util.stream.Collectors;


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

    public static Robot convertToRobot(List<RobotProperty> robotProperties) {
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

    public static RobotData mapToRobotData(List<RobotProperty> properties) {
        List<Object[]> speedList = new ArrayList<>();
        List<Object[]> batteryList = new ArrayList<>();

        for (RobotProperty property : properties) {
            long timestamp = property.getTimestamp().getTime();
            Object[] data = {timestamp, parseValue(property.getValue())};

            if (property.getType().equals(TypeProperty.SPEED)) {
                speedList.add(data);
            } else if (property.getType().equals(TypeProperty.LEVEL_BATTERY)) {
                batteryList.add(data);
            }
        }

        Object[][] speedArray = speedList.toArray(new Object[0][]);
        Object[][] batteryArray = batteryList.toArray(new Object[0][]);

        return new RobotData(speedArray, batteryArray);
    }
    private static double parseValue(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // Handle parsing error, return NaN or any other appropriate value
            return Double.NaN;
        }
    }
}
