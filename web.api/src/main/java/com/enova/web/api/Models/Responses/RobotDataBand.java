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
    Set<PlotBand> desconnected;
    Set<PlotBand> connected;
    Set<PlotBand> manual;
    Set<PlotBand> auto;
    Set<PlotBand> normal;
    Set<PlotBand> ems;
    Set<PlotBand> pause;
    Set<PlotBand> inactive;
    Set<PlotBand> waiting;
    Set<PlotBand> running;
    Set<PlotBand> charge;
    Set<PlotBand> discharge;
    Set<PlotBand> maxSpeed;
    Set<PlotBand> minSpeed;
    Set<PlotBand> normalSpeed;
}