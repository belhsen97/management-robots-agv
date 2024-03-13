package com.enova.web.api.Controllers;

import com.enova.web.api.Dtos.WorkstationDto;
import com.enova.web.api.Entitys.Tag;
import com.enova.web.api.Entitys.Workstation;
import com.enova.web.api.Mappers.WorkstationMapper;
import com.enova.web.api.Services.ITagService;
import com.enova.web.api.Services.IWorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<Tag> SelectAll() {return iService.selectAll(); }

    @PostMapping
    public Tag Insert(@RequestBody Tag t) {return iService.insert(t);  }

    @PutMapping("{id}")
    public ResponseEntity<Tag> update(@PathVariable("id") String id, @RequestBody Tag t) {
        return ResponseEntity.ok(iService.update( id,t));
    }
}
