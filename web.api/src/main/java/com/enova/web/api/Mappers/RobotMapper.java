package com.enova.web.api.Mappers;

import com.enova.web.api.Enums.*;
import com.enova.web.api.Models.Dtos.RobotDto;
import com.enova.web.api.Models.Dtos.WorkstationDto;
import com.enova.web.api.Models.Entitys.Robot;
import com.enova.web.api.Models.Entitys.RobotProperty;
import com.enova.web.api.Models.Entitys.Workstation;
import com.enova.web.api.Models.Responses.RobotData;

import java.util.*;


public class RobotMapper {
    public static Robot mapToEntity(RobotDto r) {
        final Workstation w = r.getWorkstation() == null ? null : WorkstationMapper.mapToEntity(r.getWorkstation());
        final String name = w == null ? null : w.getName() == null ? null : w.getName();
        return Robot.builder()
                .createdAt(r.getCreatedAt())
                .name(r.getName())
                .statusRobot(r.getStatusRobot())
                .modeRobot(r.getModeRobot())
                .connection(r.getConnection())
                .operationStatus(r.getOperationStatus())
                .levelBattery(r.getLevelBattery())
                .speed(r.getSpeed())
                .nameWorkstation(name)
                .build();
    }

    public static RobotDto mapToDto(Robot r) {
        if (r == null) {
            return null;
        }
        final WorkstationDto w = r.getWorkstation() == null ? null : WorkstationMapper.mapToDto(r.getWorkstation());
        return RobotDto.builder()
                .id(r.getId())
                .createdAt(r.getCreatedAt())
                .name(r.getName())
                .statusRobot(r.getStatusRobot())
                .modeRobot(r.getModeRobot())
                .connection(r.getConnection())
                .operationStatus(r.getOperationStatus())
                .levelBattery(r.getLevelBattery())
                .speed(r.getSpeed())
                .workstation(w)
                .build();
    }


    public static Robot mapRobotPropertyToRobot(List<RobotProperty> robotProperties) {
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

    public static List<RobotData> mapToRobotData(List<RobotProperty> properties) {
        if (properties.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<RobotProperty>> propertyMap = new HashMap<>();
        for (RobotProperty property : properties) {
            propertyMap.computeIfAbsent(property.getName(), k -> new ArrayList<>()).add(property);
        }

        List<RobotData> robotDataList = new ArrayList<>();
        for (Map.Entry<String, List<RobotProperty>> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            List<RobotProperty> props = entry.getValue();
            robotDataList.add(mapToRobotData(name, props));
        }

        return robotDataList;
    }


    public static RobotData mapToRobotData(String name, List<RobotProperty> properties) {
        List<Object[]> speedList = new ArrayList<>();
        List<Object[]> batteryList = new ArrayList<>();
        List<Object[]> statusList = new ArrayList<>();
        List<Object[]> operationStatusList = new ArrayList<>();

        Object[] data = new Object[5];
        data[0] = new Date().getTime();
        data[1] = Double.NaN;
        data[2] = Double.NaN;
        data[3] =  StatusRobot.INACTIVE.getValue();
        data[4] =  OperationStatus.NORMAL.getValue();
        for (RobotProperty property : properties) {
            data[0] = property.getTimestamp().getTime();
            switch (property.getType()) {
                case SPEED:
                    data[1] = parseDoubleValue(property.getValue());
                    speedList.add(new Object[]{data[0], data[1]});
                    break;
                case LEVEL_BATTERY:
                    data[2] = parseDoubleValue(property.getValue());
                    batteryList.add(new Object[]{data[0], data[2]});
                    break;
                case STATUS_ROBOT:
                    data[3] =  parseStatusValue(property.getValue());
                    statusList.add(new Object[]{data[0], data[3]});
                    break;
                case OPERATION_STATUS:
                    data[4] = parseOperationStatusValue(property.getValue());
                    operationStatusList.add(new Object[]{data[0], data[4]});
                    break;
            }
        }
        if (!properties.isEmpty()) {
            speedList.add(new Object[]{data[0], data[1]});
            batteryList.add(new Object[]{data[0], data[2]});
            statusList.add(new Object[]{data[0], data[3]});
            operationStatusList.add(new Object[]{data[0], data[4]});
        }
        Object[][] speedArray = speedList.toArray(new Object[0][]);
        Object[][] batteryArray = batteryList.toArray(new Object[0][]);
        Object[][] statusArray = statusList.toArray(new Object[0][]);
        Object[][] operationStatusArray = operationStatusList.toArray(new Object[0][]);
        return new RobotData(name, speedArray, batteryArray, statusArray, operationStatusArray);
    }

    private static double parseDoubleValue(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    private static int parseStatusValue(String str) {
        try {
            return StatusRobot.valueOf(str).getValue();
        } catch (IllegalArgumentException e) {
            return StatusRobot.INACTIVE.getValue();
        }
    }

    private static int parseOperationStatusValue(String str) {
        try {
            return OperationStatus.valueOf(str).getValue();
        } catch (IllegalArgumentException e) {
            return OperationStatus.NORMAL.getValue();
        }
    }

}