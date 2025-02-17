package tn.enova.Controllers;


import tn.enova.Enums.ReponseStatus;
import tn.enova.Mappers.WorkstationMapper;
import tn.enova.Models.Dtos.WorkstationDto;
import tn.enova.Models.Entitys.Workstation;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Services.WorkstationService;
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
    public List<WorkstationDto> GetAll() {
        final List<Workstation> list = iService.selectAll();
        return list.stream().map(w -> WorkstationMapper.mapToDto(w)).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<WorkstationDto> SelectBy(@PathVariable String id) {
        return ResponseEntity.ok(WorkstationMapper.mapToDto(iService.selectById(id)));
    }

    @PostMapping
    public WorkstationDto Add(@RequestBody WorkstationDto wd) {
        return WorkstationMapper.mapToDto(iService.insert(WorkstationMapper.mapToEntity(wd)));
    }

    @PutMapping("{id}")
    public ResponseEntity<WorkstationDto> Update(@PathVariable String id, @RequestBody WorkstationDto wd) {
        return ResponseEntity.ok(WorkstationMapper.mapToDto(iService.update(id, WorkstationMapper.mapToEntity(wd))));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> Delete(@PathVariable String id) {
        iService.delete(id);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete Workstation")
                .message("SUCCESSFUL to delete Workstation : " + id)
                .datestamp(new Date()).build());
    }
}