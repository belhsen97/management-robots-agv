package com.enova.statsync.api.Models.Responses;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RobotData {
    //String name;
    Object[][] speed ;//= {{1647437400000L, 8.59},};
    Object[][] battery ;//= { {1647523800000L,90.5},};
}
