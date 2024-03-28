package com.enova.web.api.Controllers;


import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Enums.ReponseStatus;
import com.enova.web.api.Models.Dtos.WorkstationDto;
import com.enova.web.api.Models.Entitys.Workstation;
import com.enova.web.api.Mappers.WorkstationMapper;
import com.enova.web.api.Services.WorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/workstation")
public class WorkstationController {
    private final WorkstationService iService;

    @Autowired
    public WorkstationController(@Qualifier("workstation-service") WorkstationService iService) {
        this.iService = iService;
    }

    @GetMapping
    public List<WorkstationDto> SelectAll() {
        final List<Workstation> list = iService.selectAll();
        return list.stream().map(w -> WorkstationMapper.mapToDto(w)).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<WorkstationDto> SelectBy(@PathVariable String id) {
        return ResponseEntity.ok(WorkstationMapper.mapToDto(iService.selectById(id)));
    }

    @PostMapping
    public WorkstationDto Insert(@RequestBody WorkstationDto wd) {
        return WorkstationMapper.mapToDto(iService.insert(WorkstationMapper.mapToEntity(wd)));
    }

    @PutMapping("{id}")
    public ResponseEntity<WorkstationDto> update(@PathVariable String id, @RequestBody WorkstationDto wd) {
        return ResponseEntity.ok(WorkstationMapper.mapToDto(iService.update(id, WorkstationMapper.mapToEntity(wd))));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> delete(@PathVariable String id) {
        iService.delete(id);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete Workstation")
                .message("SUCCESSFUL to delete Workstation : " + id)
                .datestamp(new Date()).build());
    }
}