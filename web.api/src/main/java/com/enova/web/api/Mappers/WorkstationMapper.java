package com.enova.web.api.Mappers;

import com.enova.web.api.Dtos.RobotDto;
import com.enova.web.api.Dtos.WorkstationDto;
import com.enova.web.api.Entitys.Robot;
import com.enova.web.api.Entitys.Workstation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkstationMapper {
    public static Workstation mapToEntity(WorkstationDto wd) {
        Set<Robot> robotList = new HashSet<Robot>();
        robotList = (
                (wd.getRobots() == null || wd.getRobots().isEmpty())
                        ?
                        robotList
                        :
                        wd.getRobots()
                                .stream()
                                .map(robot -> RobotMapper.mapToEntity(robot))
                                .collect(Collectors.toSet())
        );
        return Workstation.builder()
                .name(wd.getName())
                .enable(wd.isEnable())
                .robots(robotList)
                //.tags(wd.getTags())
                .build();
    }

    public static WorkstationDto mapToDto(Workstation w) {
        if (  w == null ) { return null; }
        List<RobotDto> robotDtoList = new ArrayList<RobotDto>();
        robotDtoList = (
                (w.getRobots() == null || w.getRobots().isEmpty())
                        ?
                        robotDtoList
                        :
                        w.getRobots()
                                .stream()
                                .map(robot -> RobotMapper.mapToDto(robot))
                                .collect(Collectors.toList())


        );
        return WorkstationDto.builder()
                .id(w.getId())
                .name(w.getName())
                .enable(w.isEnable())
                .tags(w.getTags())
                .robots(robotDtoList)
                .build();
    }
}
