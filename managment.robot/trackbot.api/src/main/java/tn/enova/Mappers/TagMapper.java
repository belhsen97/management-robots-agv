package tn.enova.Mappers;


import tn.enova.Models.Dtos.TagDto;
import tn.enova.Models.Dtos.WorkstationDto;
import tn.enova.Models.Entitys.Tag;
import tn.enova.Models.Entitys.Workstation;

public class TagMapper {
    public static Tag mapToEntity(TagDto tdto) {
        final Workstation w =  tdto.getWorkstation() == null ? null : WorkstationMapper.mapToEntity(tdto.getWorkstation()) ;
        final String name =  w == null ? null : w.getName() == null ? null : w.getName();
        return Tag.builder()
                .code(tdto.getCode())
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
                .workstation(w)
                .build();
    }
}
