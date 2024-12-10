package tn.enova.Controllers;

import tn.enova.Mappers.RobotSettingMapper;
import tn.enova.Models.Dtos.RobotSettingDto;
import tn.enova.Models.Entitys.RobotSetting;
import tn.enova.Services.RobotSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/robot-setting")
public class RobotConfigController {
    private final RobotSettingService service;

    @Autowired
    public RobotConfigController(@Qualifier("robot-setting-service") RobotSettingService service) {
        this.service = service;
    }

    @GetMapping
    public RobotSettingDto Get() {
        final List<RobotSetting> list = service.selectAll();
        return RobotSettingMapper.mapToDto(list);
    }

    @GetMapping("/list")
    public List<RobotSetting> GetList() {
        return service.selectAll();
    }

    @PutMapping()
    public ResponseEntity<RobotSettingDto> Update(@RequestBody RobotSettingDto rDto) {
        return ResponseEntity.ok(RobotSettingMapper.mapToDto(service.update(RobotSettingMapper.mapToEntity(rDto))));
    }
}
