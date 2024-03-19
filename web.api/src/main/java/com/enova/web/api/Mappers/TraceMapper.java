package com.enova.web.api.Mappers;

import com.enova.web.api.Dtos.TraceDto;
import com.enova.web.api.Dtos.UserDto;
import com.enova.web.api.Entitys.Trace;

public class TraceMapper {
    public static TraceDto mapToDto(Trace t) {
        if (  t == null ) { return null; }
        final UserDto uDto = t.getUser() == null ? null : UserMapper.mapToDto(t.getUser());
        return TraceDto.builder()
                .id(t.getId())
                .username(t.getUsername())
                .timestamp(t.getTimestamp())
                .description(t.getDescription())
                .className(t.getClassName())
                .methodName(t.getMethodName())
                .user(uDto)
                .build();
    }
}

