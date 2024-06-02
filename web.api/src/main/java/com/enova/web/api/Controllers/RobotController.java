package com.enova.web.api.Controllers;


import com.enova.web.api.Mappers.RobotPropertyMapper;
import com.enova.web.api.Mappers.RobotSettingMapper;
import com.enova.web.api.Models.Entitys.RobotProperty;
import com.enova.web.api.Models.Entitys.RobotSetting;
import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Enums.ReponseStatus;
import com.enova.web.api.Models.Responses.RobotDataBand;
import com.enova.web.api.Models.Responses.RobotDataChart;
import com.enova.web.api.Models.Dtos.RobotDto;
import com.enova.web.api.Models.Entitys.Robot;
import com.enova.web.api.Mappers.RobotMapper;
import com.enova.web.api.Services.RobotService;
import com.enova.web.api.Services.RobotSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/robot")
public class RobotController {
    private final RobotService iService;
    private final RobotSettingService irobotSettingService;
    @Autowired
    public RobotController(@Qualifier("robot-service") RobotService iService,
   @Qualifier("robot-setting-service") RobotSettingService irobotSettingService) {
        this.iService = iService;
        this.irobotSettingService = irobotSettingService;
    }

    @GetMapping
    public List<RobotDto> GetAll() {
        final List<Robot> list = iService.selectAll();
        return list.stream().map(r -> RobotMapper.mapToDto(r)).collect(Collectors.toList());
    }


    @GetMapping("{id}")
    public ResponseEntity<RobotDto> GetById(@PathVariable String id) {
        return ResponseEntity.ok(RobotMapper.mapToDto(iService.selectById(id)));
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<RobotDto> GetByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(RobotMapper.mapToDto(iService.selectByName(name)));
    }

    @Async("get-robot-data")
    @GetMapping("/databand")
    public CompletableFuture<?> GetAllOrByNameOrUnixTimestampsRobotDataBand(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "start", required = false)  Long  start,
            @RequestParam(value = "end", required = false)  Long  end) {
        final List<RobotProperty> list  = iService.selectDataPropertysAllOrByNameOrUnixTimestamps(name,start,end);
        final List<RobotSetting> listSetting = irobotSettingService.selectAll();
        RobotPropertyMapper.globalSetting = RobotSettingMapper.mapToDto(listSetting);

        if ( name == null  ){
            final  List<RobotDataBand> listRobotData  = RobotPropertyMapper.mapToRobotsDataBand(list);
            return CompletableFuture.completedFuture(listRobotData);
        }
        final  RobotDataBand r  = RobotPropertyMapper.mapToRobotDataBand(name , list);
        return CompletableFuture.completedFuture(r);
    }
    @Async("get-robot-data")
    @GetMapping("/property")
    public CompletableFuture<List<RobotProperty>> GetAllPropertyByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "start", required = false)  Long  start,
            @RequestParam(value = "end", required = false)  Long  end){
        final List<RobotProperty> list  =  iService.selectDataPropertysAllOrByNameOrUnixTimestamps(name,start,end);
        return CompletableFuture.completedFuture(list);
    }
    @Async("get-robot-data")
    @GetMapping("/data-chart")
    public CompletableFuture<?> GetDataChartbyNameAndUnixTimeStamps(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "start", required = false)  Long  start,
            @RequestParam(value = "end", required = false)  Long  end){
        final List<RobotProperty> list  =  iService.selectDataPropertysAllOrByNameOrUnixTimestamps(name,start,end);
        if ( name == null  ){
            final  List<RobotDataChart> listRobotDataChart  = RobotPropertyMapper.mapToRobotsDataChart(list);
            return CompletableFuture.completedFuture(listRobotDataChart);
        }
        final  RobotDataChart r  = RobotPropertyMapper.mapToRobotDataChart(name , list);
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

