package com.enova.statsync.api.Controllers;


import com.enova.statsync.api.Mappers.RobotMapper;
import com.enova.statsync.api.Models.Entitys.RobotProperty;
import com.enova.statsync.api.Models.Responses.RobotData;
import com.enova.statsync.api.Services.RobotService;
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
    private final RobotService iService;

    @Autowired
    public RobotController(@Qualifier("robot-service") RobotService iService) {
        this.iService = iService;
    }

    @GetMapping
    public List<RobotProperty> SelectAll() {
        return iService.selectAllDataPropertys();
    }
    @GetMapping("/name/{name}")
    @ResponseBody
    public  List<RobotProperty>   SelectBy(@PathVariable String name) {
        return iService.selectAllDataPropertysByName(name);
    }
    @GetMapping("/RobotData")
    @ResponseBody
    public RobotData SelectRobotDataByName(@RequestParam("name") String name) {
        return RobotMapper.mapToRobotData(iService.selectAllDataPropertysByName(name));
    }
}

