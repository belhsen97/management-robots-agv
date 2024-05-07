package com.enova.web.api.Controllers;

import com.enova.web.api.Mappers.RobotSettingMapper;
import com.enova.web.api.Models.Dtos.RobotSettingDto;
import com.enova.web.api.Models.Entitys.RobotSetting;
import com.enova.web.api.Services.RobotSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/robot-setting")
public class RobotConfigController {
    private final RobotSettingService iService;

    @Autowired
    public RobotConfigController(@Qualifier("robot-setting-service") RobotSettingService iService) {
        this.iService = iService;
    }

    @GetMapping
    public RobotSettingDto Get() {
        final List<RobotSetting> list = iService.selectAll();
        return RobotSettingMapper.mapToDto(list);
    }

    @PutMapping()
    public ResponseEntity<RobotSettingDto> Update(@RequestBody RobotSettingDto rDto) {
        return ResponseEntity.ok(RobotSettingMapper.mapToDto(iService.update(RobotSettingMapper.mapToEntity(rDto))));
    }
}
