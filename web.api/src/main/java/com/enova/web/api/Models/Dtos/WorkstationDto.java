package com.enova.web.api.Models.Dtos;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    List<TagDto> tags ;
}