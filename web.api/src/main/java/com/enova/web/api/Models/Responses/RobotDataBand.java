package com.enova.web.api.Models.Responses;


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
public class RobotDataBand {
    String name;
    List<PlotBand> desconnected;
    List<PlotBand> connected;
    List<PlotBand> manual;
    List<PlotBand> auto;
    List<PlotBand> normal;
    List<PlotBand> ems;
    List<PlotBand> pause;
    List<PlotBand> inactive;
    List<PlotBand> waiting;
    List<PlotBand> running;
    List<PlotBand> charge;
    List<PlotBand> discharge;
    List<PlotBand> standby;
    List<PlotBand> maxSpeed;
    List<PlotBand> minSpeed;
    List<PlotBand> normalSpeed;
}