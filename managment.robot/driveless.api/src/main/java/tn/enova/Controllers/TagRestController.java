package tn.enova.Controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enova.Enums.ReponseStatus;
import tn.enova.Models.Commons.Tag;
import tn.enova.Models.Responses.MsgReponseStatus;
import tn.enova.Services.TagService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagRestController {
    private final TagService service;

    @GetMapping()
    public List<Tag> GetAll() {
        return service.selectAll();
    }

    @GetMapping("/code-all")
    public List<String> GetAllCode() {
        return service.selectAllCode();
    }

    @PostMapping
    public ResponseEntity<MsgReponseStatus> Add(@RequestBody Tag tag) {
        service.insert(tag);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Add Tag")
                .message("SUCCESSFUL to Add new Tag")
                .datestamp(new Date()).build());
    }

    @PutMapping("{id}")
    public ResponseEntity<MsgReponseStatus> Update(@PathVariable("id") String id, @RequestBody Tag tag) {
        service.update(id,tag);
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
