package tn.enova.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enova.Enums.ReponseStatus;
import tn.enova.Models.Commons.RobotSetting;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Services.RobotSettingService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/robot-setting")
@RequiredArgsConstructor
public class RobotConfigRestController {

    private final RobotSettingService service;


    @GetMapping("/speed/average")
    public ResponseEntity<Map> GetSpeedAverage() {
        Double speedValue = service.selectSpeedAverage();
        Map<String, Object> map = new HashMap<>();
        map.put("value", speedValue);
        return ResponseEntity.ok(map);
    }


    @PostMapping
    public ResponseEntity<MsgReponseStatus> Add(@RequestBody RobotSetting robotSetting) {
        service.insert(robotSetting);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Add RobotSetting")
                .message("SUCCESSFUL to Add new RobotSetting")
                .datestamp(new Date()).build());
    }

    @PutMapping("{id}")
    public ResponseEntity<MsgReponseStatus> Update(@PathVariable("id") String id, @RequestBody RobotSetting robotSetting) {
        service.update(id,robotSetting);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Update Tag")
                .message("SUCCESSFUL to Update RobotSetting : " + id)
                .datestamp(new Date()).build());
    }
    @PutMapping()
    public ResponseEntity<MsgReponseStatus> UpdateMultiple(@RequestBody List<RobotSetting> list) {
       service.update(list);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Update Tag")
                .message("SUCCESSFUL to Update list of RobotSettings")
                .datestamp(new Date()).build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> Delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete Tag")
                .message("SUCCESSFUL to delete RobotSetting : " + id)
                .datestamp(new Date()).build());
    }
    @DeleteMapping()
    public ResponseEntity<MsgReponseStatus> DeleteAll() {
        service.deleteAll();
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete Tag")
                .message("SUCCESSFUL to delete All RobotSettings")
                .datestamp(new Date()).build());
    }


}
