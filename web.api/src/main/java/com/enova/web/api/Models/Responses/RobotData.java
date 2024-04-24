package com.enova.web.api.Models.Responses;

import com.enova.web.api.Enums.TypeProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RobotData {
    String name;
    private long from;
    private long to;
    Date timestamp;
    TypeProperty type;
    String value;
    //not complete 
}