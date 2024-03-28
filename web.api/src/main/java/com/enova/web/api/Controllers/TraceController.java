package com.enova.web.api.Controllers;

import com.enova.web.api.Models.Responses.MsgReponseStatus;
import com.enova.web.api.Enums.ReponseStatus;
import com.enova.web.api.Models.Dtos.TraceDto;
import com.enova.web.api.Models.Entitys.Trace;
import com.enova.web.api.Mappers.TraceMapper;
import com.enova.web.api.Services.TraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trace")
public class TraceController {
    private final TraceService iService;
    @Autowired
    public TraceController(@Qualifier("trace-service") TraceService iService) {
        this.iService = iService;
    }

    @GetMapping
    public List<TraceDto> SelectAll() {
        final List<Trace> list = iService.selectAll();
        return list.stream().map(t -> TraceMapper.mapToDto(t)).collect(Collectors.toList());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MsgReponseStatus> delete(@PathVariable String id) {
        iService.delete(id);
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete Trace")
                .message("SUCCESSFUL to delete Trace : " + id)
                .datestamp(new Date()).build());
    }
    @DeleteMapping
    public ResponseEntity<MsgReponseStatus> delete() {
        iService.selectAll();
        return ResponseEntity.ok(MsgReponseStatus.builder()
                .status(ReponseStatus.SUCCESSFUL)
                .title("Delete Trace")
                .message("SUCCESSFUL to delete All Traces")
                .datestamp(new Date()).build());
    }
}
