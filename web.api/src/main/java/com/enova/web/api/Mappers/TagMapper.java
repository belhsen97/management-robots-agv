package com.enova.web.api.Mappers;


import com.enova.web.api.Models.Dtos.TagDto;
import com.enova.web.api.Models.Dtos.WorkstationDto;
import com.enova.web.api.Models.Entitys.Tag;
import com.enova.web.api.Models.Entitys.Workstation;

public class TagMapper {
    public static Tag mapToEntity(TagDto tdto) {
        final Workstation w =  tdto.getWorkstation() == null ? null : WorkstationMapper.mapToEntity(tdto.getWorkstation()) ;
        final String name =  w == null ? null : w.getName() == null ? null : w.getName();
        return Tag.builder()
                .code(tdto.getCode())
                .description(tdto.getDescription())
                .workstationName(name)
                .workstation(null)
                .build();
    }

    public static TagDto mapToDto(Tag t) {
        if (  t == null ) { return null; }
        final WorkstationDto w = t.getWorkstation() == null ? null : WorkstationMapper.mapToDto(t.getWorkstation());
        return TagDto.builder()
                .id(t.getId())
                .code(t.getCode())
                .description(t.getDescription())
                .workstation(w)
                .build();
    }
}
