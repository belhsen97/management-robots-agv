package com.enova.web.api.Mappers;

import com.enova.web.api.Dtos.RobotDto;
import com.enova.web.api.Dtos.WorkstationDto;
import com.enova.web.api.Entitys.Workstation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorkstationMapper {
    public static Workstation mapToEntity(WorkstationDto r) {
        return Workstation.builder()
                .id(r.getId())
                .name(r.getName())
                .enable(r.isEnable())
                .tags(r.getTags())
                .build();
    }

    public static WorkstationDto mapToDto(Workstation r) {
        List<RobotDto> robotDtoList = new ArrayList<RobotDto>();
        robotDtoList = (
                (r.getRobots() != null && !r.getRobots().isEmpty())
                        ?
                        r.getRobots()
                                .stream()
                                .map(robot -> RobotMapper.mapToDto(robot))
                                .collect(Collectors.toList())
                        :
                        robotDtoList
        );
        return WorkstationDto.builder()
                .id(r.getId())
                .name(r.getName())
                .enable(r.isEnable())
                .tags(r.getTags())
                .robots(robotDtoList)
                .build();
    }
}
