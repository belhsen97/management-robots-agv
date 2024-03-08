package com.enova.web.api.Controllers;

import com.enova.web.api.Dtos.RobotDto;
import com.enova.web.api.Dtos.WorkstationDto;
import com.enova.web.api.Entitys.Workstation;
import com.enova.web.api.Mappers.RobotMapper;
import com.enova.web.api.Mappers.WorkstationMapper;
import com.enova.web.api.Services.IWorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    public WorkstationDto Insert(@RequestBody WorkstationDto wd) {
        return WorkstationMapper.mapToDto(iService.insert(WorkstationMapper.mapToEntity(wd)));
    }
    @GetMapping("{id}")
    public ResponseEntity<WorkstationDto> SelectBy(@PathVariable String id) {
        return ResponseEntity.ok(WorkstationMapper.mapToDto(iService.selectById(id)));
    }

}