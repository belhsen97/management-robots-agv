package tn.enova.Controllers;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.enova.Configures.ParameterConfig;
import tn.enova.Enums.ReponseStatus;
import tn.enova.Libs.MockMultipartFile;
import tn.enova.Mappers.RobotMapper;
import tn.enova.Mappers.RobotPropertyMapper;
import tn.enova.Mappers.RobotSettingMapper;
import tn.enova.Models.Commons.Attachment;
import tn.enova.Models.Dtos.RobotDto;
import tn.enova.Models.Entitys.Robot;
import tn.enova.Models.Entitys.RobotProperty;
import tn.enova.Models.Entitys.RobotSetting;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Models.Responses.RobotDataBand;
import tn.enova.Models.Responses.RobotDataChart;
import tn.enova.Services.FileService;
import tn.enova.Services.RobotService;
import tn.enova.Services.RobotSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/robot")
public class RobotController {
    private final RobotService iService;

    private final RobotSettingService irobotSettingService;

    private final ParameterConfig parameterConfig;
    private final FileService fileService;

    @Autowired
    public RobotController(@Qualifier("robot-service") RobotService iService,
   @Qualifier("robot-setting-service") RobotSettingService irobotSettingService, ParameterConfig parameterConfig,@Qualifier("file-service")  FileService fileService) {
        this.iService = iService;
        this.irobotSettingService = irobotSettingService;
        this.parameterConfig = parameterConfig;
        this.fileService = fileService;
    }



    @GetMapping("/get-standart-photo")
    public ResponseEntity<Resource> downloadStantardPhoto() throws Exception {
            Attachment img = fileService.downloadStantardPhoto(parameterConfig.defaultRobotPhoto);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(img.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + img.getFileName() + "\"")
                    .body(new ByteArrayResource(img.getData()));
    }







    @GetMapping
    public List<RobotDto> GetAll() {
        final List<Robot> list = iService.selectAll();
        return list.stream().map(r -> RobotMapper.mapToDto(r)).collect(Collectors.toList());
    }
    @GetMapping("/ids/{ids}")
    public List<RobotDto> GetAllByIds(@PathVariable(name = "ids") List<String> ids) {
        final List<Robot> list = iService.selectAllByIds(ids);
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

    @Async("robot-data")
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
    @Async("robot-data")
    @GetMapping("/property")
    public CompletableFuture<List<RobotProperty>> GetAllPropertyByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "start", required = false)  Long  start,
            @RequestParam(value = "end", required = false)  Long  end){
        final List<RobotProperty> list  =  iService.selectDataPropertysAllOrByNameOrUnixTimestamps(name,start,end);
        return CompletableFuture.completedFuture(list);
    }
    @Async("robot-data")
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





    @GetMapping("/ping/{ip}")
    public String ping(@PathVariable String ip) {
        System.out.println("|"+ip+"|");
        try {
            InetAddress inet = InetAddress.getByName(ip);
            return "Ping to " + ip + (inet.isReachable(10000) ? " was successful." : "failed fff");
        } catch (IOException e) {
            e.printStackTrace();
            return "Ping to " + ip + " failed.";
        }
    }
}

