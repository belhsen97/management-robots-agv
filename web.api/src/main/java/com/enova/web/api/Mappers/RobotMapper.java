package com.enova.web.api.Mappers;

import com.enova.web.api.Models.Dtos.RobotDto;
import com.enova.web.api.Models.Dtos.WorkstationDto;
import com.enova.web.api.Models.Entitys.Robot;
import com.enova.web.api.Models.Entitys.Workstation;

public class RobotMapper {
    public static Robot mapToEntity(RobotDto r) {
        final Workstation w =  r.getWorkstation() == null ? null : WorkstationMapper.mapToEntity(r.getWorkstation()) ;
        final String name =  w == null ? null : w.getName() == null ? null : w.getName();
        return Robot.builder()
                .createdAt(r.getCreatedAt())
                .name(r.getName())
                .statusRobot(r.getStatusRobot())
                .modeRobot(r.getModeRobot())
                .connection(r.getConnection())
                .operationStatus(r.getOperationStatus())
                .levelBattery(r.getLevelBattery())
                .speed(r.getSpeed())
                .idWorkstation(name)
                .build();
    }

    public static RobotDto mapToDto(Robot r) {
        if (  r == null ) { return null; }
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
}