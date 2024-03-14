package com.enova.web.api.Controllers;

import com.enova.web.api.Dtos.MsgReponseStatus;
import com.enova.web.api.Dtos.ReponseStatus;
import com.enova.web.api.Dtos.TagDto;
import com.enova.web.api.Dtos.WorkstationDto;
import com.enova.web.api.Entitys.Tag;
import com.enova.web.api.Entitys.Workstation;
import com.enova.web.api.Mappers.TagMapper;
import com.enova.web.api.Mappers.WorkstationMapper;
import com.enova.web.api.Services.ITagService;
import com.enova.web.api.Services.IWorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tag")
public class TagController {
    private final ITagService iService;
    @Autowired
    public TagController(@Qualifier("tag-service") ITagService iService) {
        this.iService = iService;
    }

    @GetMapping
    public List<TagDto> SelectAll() {
        final List<Tag> list = iService.selectAll();
        return list.stream().map(t -> TagMapper.mapToDto(t)).collect(Collectors.toList());
    }

    @PostMapping
    public TagDto Insert(@RequestBody TagDto t) {
        return TagMapper.mapToDto(iService.insert(TagMapper.mapToEntity(t)));
    }

    @PutMapping("{id}")
    public ResponseEntity<TagDto> update(@PathVariable("id") String id, @RequestBody TagDto t) {
        return ResponseEntity.ok(TagMapper.mapToDto(iService.update(id, TagMapper.mapToEntity(t))));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> delete(@PathVariable String id) {
        if (iService.delete(id)) {
            return ResponseEntity.ok(
                    MsgReponseStatus.builder()
                            .status(ReponseStatus.SUCCESSFUL)
                            .title("Delete user")
                            .message("SUCCESSFUL to delete user : " + id)
                            .datestamp(new Date()).build());
        }
        return ResponseEntity.ok(
                MsgReponseStatus.builder()
                        .status(ReponseStatus.UNSUCCESSFUL)
                        .title("Delete user")
                        .message("UNSUCCESSFUL to delete user : " + id)
                        .datestamp(new Date()).build());
    }
}
