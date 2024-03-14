package com.enova.web.api.Controllers;

import com.enova.web.api.Dtos.*;
import com.enova.web.api.Entitys.Robot;
import com.enova.web.api.Mappers.RobotMapper;
import com.enova.web.api.Services.IRobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/robot")
public class RobotController {
    private final IRobotService iService;


    @Autowired
    public RobotController(@Qualifier("robot-service") IRobotService iService) {
        this.iService = iService;
    }

    @GetMapping
    public List<RobotDto> SelectAll() {
        final List<Robot> list = iService.selectAll();
        return list.stream().map(r -> RobotMapper.mapToDto(r)).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<RobotDto> SelectBy(@PathVariable String id) {
        return ResponseEntity.ok(RobotMapper.mapToDto(iService.selectById(id)));
    }

    @PostMapping
    public RobotDto Insert(@RequestBody RobotDto rd) {
        return RobotMapper.mapToDto(iService.insert(RobotMapper.mapToEntity(rd)));
    }

    @PutMapping("{id}")
    public ResponseEntity<RobotDto> update(@PathVariable String id, @RequestBody RobotDto rd) {
        return ResponseEntity.ok(RobotMapper.mapToDto(iService.update(id, RobotMapper.mapToEntity(rd))));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> delete(@PathVariable String id) {
        iService.delete(id);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete robot")
                .message("SUCCESSFUL to delete robot : " + id)
                .datestamp(new Date()).build());
    }

}

