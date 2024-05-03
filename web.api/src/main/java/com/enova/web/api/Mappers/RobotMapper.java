package com.enova.web.api.Mappers;

import com.enova.web.api.Enums.*;
import com.enova.web.api.Models.Dtos.RobotDto;
import com.enova.web.api.Models.Dtos.WorkstationDto;
import com.enova.web.api.Models.Entitys.Robot;
import com.enova.web.api.Models.Entitys.RobotProperty;
import com.enova.web.api.Models.Entitys.Workstation;

import java.util.*;


public class RobotMapper {
    public static Robot mapToEntity(RobotDto r) {
        final Workstation w = r.getWorkstation() == null ? null : WorkstationMapper.mapToEntity(r.getWorkstation());
        final String name = w == null ? null : w.getName() == null ? null : w.getName();
        return Robot.builder()
                .clientid(r.getClientid())
                .username(r.getUsername())
                .password(r.getPassword())
                .createdAt(r.getCreatedAt())
                .name(r.getName())
                .notice(r.getNotice())
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
        if (r == null) {  return null; }
        final WorkstationDto w = r.getWorkstation() == null ? null : WorkstationMapper.mapToDto(r.getWorkstation());
        return RobotDto.builder()
                .id(r.getId())
                .clientid(r.getClientid())
                .username(r.getUsername())
                .password(r.getPassword())
                .createdAt(r.getCreatedAt())
                .name(r.getName())
                .notice(r.getNotice())
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


    private static Connection parseConnection(String str) {
        try {
            return  Connection.valueOf(str);
        } catch (IllegalArgumentException e) {
            return  Connection.DISCONNECTED;
        }
    }
}





            /*if (property.getType().equals(TypeProperty.CONNECTION)) {
                data[5] = Connection.parseValue(property.getValue());
                connectionList.add(new Object[]{data[0], data[5]});

                verifyCNX = parseConnection(property.getValue());
                if ( verifyCNX.equals(Connection.DISCONNECTED )){
                    speedList.add(new Object[]{data[0], null});
                    batteryList.add(new Object[]{data[0], null});
                    statusList.add(new Object[]{data[0], null});
                    operationStatusList.add(new Object[]{data[0], null});}
                else if ( verifyCNX.equals(Connection.CONNECTED )) {
                    speedList.add(new Object[]{data[0], data[1]});
                    batteryList.add(new Object[]{data[0], data[2]});
                    statusList.add(new Object[]{data[0], data[3]});
                    operationStatusList.add(new Object[]{data[0], data[4]});}
            }

            if (verifyCNX.equals(Connection.DISCONNECTED)) {
                continue; }

            if (property.getType().equals(TypeProperty.SPEED)) {
                data[1] = parseDoubleValue(property.getValue());
                speedList.add(new Object[]{data[0], data[1]});
            }
            if (property.getType().equals(TypeProperty.LEVEL_BATTERY)) {
                data[2] = parseDoubleValue(property.getValue());
                batteryList.add(new Object[]{data[0], data[2]});
            }
            if (property.getType().equals(TypeProperty.STATUS_ROBOT)) {
                data[3] = StatusRobot.parseValue(property.getValue());
                statusList.add(new Object[]{data[0], data[3]});
            }
            if (property.getType().equals(TypeProperty.STATUS_ROBOT)) {
                data[4] = OperationStatus.parseValue(property.getValue());
                operationStatusList.add(new Object[]{data[0], data[4]});
            }*/