package com.enova.web.api.Models.Dtos;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagDto {
    String id;
    String code;
    String description ;
    WorkstationDto workstation;
}
