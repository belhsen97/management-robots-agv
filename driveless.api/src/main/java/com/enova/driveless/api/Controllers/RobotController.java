package com.enova.driveless.api.Controllers;

import com.enova.driveless.api.Exceptions.RessourceNotFoundException;
import com.enova.driveless.api.Models.Entitys.Robot;
import com.enova.driveless.api.Services.RobotService;
import com.enova.driveless.api.Services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/robot")
@RequiredArgsConstructor
public class RobotController {

    final RobotService robotService;
    final TagService tagService;

    @GetMapping("{name}")
    public Robot GetRobot(@PathVariable(name = "name") String name) {
        return robotService.selectByName(name);
    }

    @GetMapping("all-code")
    public List<String> GetAllTags() {
        return tagService.selectAllCode(); //throw new RessourceNotFoundException("Cannot found tags");
    }
}
