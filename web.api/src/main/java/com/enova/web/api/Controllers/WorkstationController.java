package com.enova.web.api.Controllers;

import com.enova.web.api.Dtos.*;
import com.enova.web.api.Entitys.Workstation;
import com.enova.web.api.Mappers.RobotMapper;
import com.enova.web.api.Mappers.UserMapper;
import com.enova.web.api.Mappers.WorkstationMapper;
import com.enova.web.api.Services.IWorkstationService;
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
    private final IWorkstationService iService;
    @Autowired
    public WorkstationController(@Qualifier("workstation-service") IWorkstationService iService) {
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