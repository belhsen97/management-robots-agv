package com.enova.web.api.Controllers;

import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Enums.ReponseStatus;
import com.enova.web.api.Models.Dtos.TagDto;
import com.enova.web.api.Models.Entitys.Tag;
import com.enova.web.api.Mappers.TagMapper;
import com.enova.web.api.Services.TagService;
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
    private final TagService iService;
    @Autowired
    public TagController(@Qualifier("tag-service") TagService iService) {
        this.iService = iService;
    }

    @GetMapping
    public List<TagDto> GetAll() {
        final List<Tag> list = iService.selectAll();
        return list.stream().map(t -> TagMapper.mapToDto(t)).collect(Collectors.toList());
    }

    @PostMapping
    public TagDto Add(@RequestBody TagDto t) {
        return TagMapper.mapToDto(iService.insert(TagMapper.mapToEntity(t)));
    }

    @PutMapping("{id}")
    public ResponseEntity<TagDto> Update(@PathVariable("id") String id, @RequestBody TagDto t) {
        return ResponseEntity.ok(TagMapper.mapToDto(iService.update(id, TagMapper.mapToEntity(t))));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> Delete(@PathVariable String id) {
        iService.delete(id);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete Tag")
                .message("SUCCESSFUL to delete Tag : " + id)
                .datestamp(new Date()).build());
    }
}
