package com.enova.web.api.Controllers;


import com.enova.web.api.Models.Entitys.RobotProperty;
import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Enums.ReponseStatus;
import com.enova.web.api.Models.Responses.RobotDataChart;
import com.enova.web.api.Models.Dtos.RobotDto;
import com.enova.web.api.Models.Entitys.Robot;
import com.enova.web.api.Mappers.RobotMapper;
import com.enova.web.api.Services.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    public List<RobotDto> GetAll() {
        final List<Robot> list = iService.selectAll();
        return list.stream().map(r -> RobotMapper.mapToDto(r)).collect(Collectors.toList());
    }


    @GetMapping("{id}")
    public ResponseEntity<RobotDto> GetBy(@PathVariable String id) {
        return ResponseEntity.ok(RobotMapper.mapToDto(iService.selectById(id)));
    }

    @Async("get-robot-data")
    @GetMapping("/all/data")
    public CompletableFuture<List<RobotDataChart>> getAllData() {
        //System.out.println("Execute method with configured executor - "+ Thread.currentThread().getName());
        final  List<RobotDataChart> list  = RobotMapper.mapToRobotData(iService.selectAllDataPropertys());
        return CompletableFuture.completedFuture(list);
    }

    @Async("get-robot-data")
    @GetMapping("{name}/data/all")
    public CompletableFuture<RobotDataChart> GetDataByName(@PathVariable String name) {
        final List<RobotProperty> list  =  iService.selectAllDataPropertysByName(name);
        final  RobotDataChart r  = RobotMapper.mapToRobotData(name , list);
        return CompletableFuture.completedFuture(r);
    }
    @Async("get-robot-data")
    @GetMapping("{name}/data/date")
    public CompletableFuture<RobotDataChart> GetDatabyNameBetweenDates(
            @PathVariable("name") String name,
            @RequestParam("start")  @DateTimeFormat(pattern = "yyyy-MM-dd")  Date start,
            @RequestParam("end")  @DateTimeFormat(pattern = "yyyy-MM-dd")  Date end) {
        final List<RobotProperty> list  =  iService.selectDataPropertysByNameAndDateTimes(name,start,end);
        final  RobotDataChart r  = RobotMapper.mapToRobotData(name , list);
        return CompletableFuture.completedFuture(r);
    }
    @Async("get-robot-data")
    @GetMapping("{name}/data/datetime")
    public CompletableFuture<RobotDataChart> GetDatabyNameBetweenDateTimes(
            @PathVariable("name") String name,
            @RequestParam("start")  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  Date start,
            @RequestParam("end")  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  Date end) {
        final List<RobotProperty> list  =  iService.selectDataPropertysByNameAndDateTimes(name,start,end);
        final  RobotDataChart r  = RobotMapper.mapToRobotData(name , list);
        return CompletableFuture.completedFuture(r);
    }
    @Async("get-robot-data")
    @GetMapping("{name}/data/unix-timestamp")
    public CompletableFuture<RobotDataChart> GetDatabyNameAndUnixTimeStamps(
            @PathVariable("name") String name,
            @RequestParam("start")  Long  start,
            @RequestParam("end")  Long  end) {
        final List<RobotProperty> list  =  iService.selectDataPropertysByNameAndUnixTimestamps(name,start,end);
        final  RobotDataChart r  = RobotMapper.mapToRobotData(name , list);
        return CompletableFuture.completedFuture(r);
    }
    @PostMapping
    public RobotDto Add(@RequestBody RobotDto rd) {
        return RobotMapper.mapToDto(iService.insert(RobotMapper.mapToEntity(rd)));
    }

    @PutMapping("{id}")
    public ResponseEntity<RobotDto> Update(@PathVariable String id, @RequestBody RobotDto rd) {
        return ResponseEntity.ok(RobotMapper.mapToDto(iService.update(id, RobotMapper.mapToEntity(rd))));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> Delete(@PathVariable String id) {
        iService.delete(id);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete robot")
                .message("SUCCESSFUL to delete robot : " + id)
                .datestamp(new Date()).build());
    }

}

