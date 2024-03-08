package com.enova.web.api.Dtos;
import com.enova.web.api.Entitys.Tag;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkstationDto {
    String id;
    String name ;
    boolean enable ;
    Set<Tag> tags ;
    List<RobotDto> robots;
}
