package com.enova.web.api.Models.Responses;



import lombok.*;
import lombok.experimental.FieldDefaults;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RobotDataChart {
    String name;
    Object[][] speed ;//= {{1647437400000L, 8.59},};
    Object[][] battery ;//= { {1647523800000L,90.5},};
    Object[][] statusRobot ;//= { {1647523800000L,0},}; //0 eq WAITING or 1 eq RUNNING or 2  eq INACTIVE
    Object[][] operationStatus ;//= { {1647523800000L,0},}; //0 eq NORMAL or 1 eq EMS or 2  eq PAUSE
    PlotBand[] connectionPlotBand;
    PlotBand[] modePlotBand;
    PlotLine[] connectionPlotLine;
    PlotLine[] modePlotLine;
}