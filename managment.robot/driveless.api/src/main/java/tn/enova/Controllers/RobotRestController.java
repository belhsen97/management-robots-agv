package tn.enova.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enova.Enums.ReponseStatus;
import tn.enova.Models.Commons.Robot;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Services.RobotService;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RestController
@RequestMapping("/robot")
@RequiredArgsConstructor
public class RobotRestController {

    final RobotService service;
    @GetMapping("/name/{name}")
    public Robot GetRobotByName(@PathVariable(name = "name") String name) {
        return service.selectByName(name);
    }


    @PostMapping
    public ResponseEntity<MsgReponseStatus> Add(@RequestBody Robot robot) {
        service.insert(robot);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Add Tag")
                .message("SUCCESSFUL to Add new Tag")
                .datestamp(new Date()).build());
    }

    @PutMapping("{id}")
    public ResponseEntity<MsgReponseStatus> Update(@PathVariable("id") String id, @RequestBody Robot robot) {
        service.update(id,robot);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Update Tag")
                .message("SUCCESSFUL to Update Tag : " + id)
                .datestamp(new Date()).build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> Delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete Tag")
                .message("SUCCESSFUL to delete Tag : " + id)
                .datestamp(new Date()).build());
    }
    @DeleteMapping()
    public ResponseEntity<MsgReponseStatus> DeleteAll() {
        service.deleteAll();
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete Tag")
                .message("SUCCESSFUL to delete All Tag")
                .datestamp(new Date()).build());
    }



}
