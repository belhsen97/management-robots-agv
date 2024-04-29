package com.enova.web.api.Mappers;

import com.enova.web.api.Enums.*;
import com.enova.web.api.Models.Dtos.RobotDto;
import com.enova.web.api.Models.Dtos.WorkstationDto;
import com.enova.web.api.Models.Entitys.Robot;
import com.enova.web.api.Models.Entitys.RobotProperty;
import com.enova.web.api.Models.Entitys.Workstation;
import com.enova.web.api.Models.Responses.PlotBand;
import com.enova.web.api.Models.Responses.PlotLine;
import com.enova.web.api.Models.Responses.RobotDataBand;
import com.enova.web.api.Models.Responses.RobotDataChart;

import java.util.*;


public class RobotMapper {
    public static Robot mapToEntity(RobotDto r) {
        final Workstation w = r.getWorkstation() == null ? null : WorkstationMapper.mapToEntity(r.getWorkstation());
        final String name = w == null ? null : w.getName() == null ? null : w.getName();
        return Robot.builder()
                .clientid(r.getClientid())
                .username(r.getUsername())
                .password(r.getPassword())
                .createdAt(r.getCreatedAt())
                .name(r.getName())
                .notice(r.getNotice())
                .statusRobot(r.getStatusRobot())
                .modeRobot(r.getModeRobot())
                .connection(r.getConnection())
                .operationStatus(r.getOperationStatus())
                .levelBattery(r.getLevelBattery())
                .speed(r.getSpeed())
                .nameWorkstation(name)
                .build();
    }

    public static RobotDto mapToDto(Robot r) {
        if (r == null) {  return null; }
        final WorkstationDto w = r.getWorkstation() == null ? null : WorkstationMapper.mapToDto(r.getWorkstation());
        return RobotDto.builder()
                .id(r.getId())
                .clientid(r.getClientid())
                .username(r.getUsername())
                .password(r.getPassword())
                .createdAt(r.getCreatedAt())
                .name(r.getName())
                .notice(r.getNotice())
                .statusRobot(r.getStatusRobot())
                .modeRobot(r.getModeRobot())
                .connection(r.getConnection())
                .operationStatus(r.getOperationStatus())
                .levelBattery(r.getLevelBattery())
                .speed(r.getSpeed())
                .workstation(w)
                .build();
    }


    public static Robot mapRobotPropertyToRobot(List<RobotProperty> robotProperties) {
        Robot robot = new Robot();
        if (robotProperties == null && robotProperties.isEmpty()) {
            return robot;
        }
        robot.setName(robotProperties.get(0).getName());
        for (RobotProperty property : robotProperties) {
            switch (property.getType()) {
                case CONNECTION:
                    robot.setConnection(Connection.valueOf(property.getValue()));
                    break;
                case STATUS_ROBOT:
                    robot.setStatusRobot(StatusRobot.valueOf(property.getValue()));
                    break;
                case MODE_ROBOT:
                    robot.setModeRobot(ModeRobot.valueOf(property.getValue()));
                    break;
                case OPERATION_STATUS:
                    robot.setOperationStatus(OperationStatus.valueOf(property.getValue()));
                    break;
                case LEVEL_BATTERY:
                    robot.setLevelBattery(Integer.parseInt(property.getValue()));
                    break;
                case SPEED:
                    robot.setSpeed(Double.parseDouble(property.getValue()));
                    break;
            }
        }
        return robot;
    }




    public static List<RobotDataChart> mapToRobotData(List<RobotProperty> properties) {
        if (properties.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<RobotProperty>> propertyMap = new HashMap<>();
        for (RobotProperty property : properties) {
            propertyMap.computeIfAbsent(property.getName(), k -> new ArrayList<>()).add(property);
        }

        List<RobotDataChart> robotDataList = new ArrayList<>();
        for (Map.Entry<String, List<RobotProperty>> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            List<RobotProperty> props = entry.getValue();
            robotDataList.add(mapToRobotData(name, props));
        }

        return robotDataList;
    }


    public static RobotDataChart mapToRobotData(String name, List<RobotProperty> properties) {
        List<Object[]> speedList = new ArrayList<>();
        List<Object[]> batteryList = new ArrayList<>();
        List<Object[]> statusList = new ArrayList<>();
        List<Object[]> operationStatusList = new ArrayList<>();
        List<Object[]> connectionList = new ArrayList<>();
        List<PlotBand> connectionPlotBandList = new ArrayList<>();
        List<PlotBand> modePlotBandList = new ArrayList<>();
        List<PlotLine> connectionPlotLineList = new ArrayList<>();
        List<PlotLine> modePlotLineList = new ArrayList<>();


        Connection verifyCNX = Connection.DISCONNECTED;
        ModeRobot verifyMode = ModeRobot.MANUAL;

        PlotBand plotBandCnx = PlotBand.builder().text("connection").build();
        PlotBand plotBandmode = PlotBand.builder().text("mode").build();

        Object[] data = new Object[7];
        data[0] = new Date().getTime();
        plotBandCnx.setFrom(data[0]);
        plotBandmode.setFrom(data[0]);
        data[1] = Double.NaN;
        data[2] = Double.NaN;
        data[3] = StatusRobot.INACTIVE.getValue();
        data[4] = OperationStatus.NORMAL.getValue();
        data[5] = Connection.CONNECTED.getValue();
        data[6] = ModeRobot.MANUAL.getValue();
        for (RobotProperty property : properties) {
            data[0] = property.getTimestamp().getTime();
            switch (property.getType()) {
                case MODE_ROBOT:
                    data[6] = ModeRobot.parseValue(property.getValue());
                    if (!verifyMode.equals(ModeRobot.valueOf(property.getValue()))) {
                        verifyMode = ModeRobot.valueOf(property.getValue());
                    }
                    if (verifyMode.equals(ModeRobot.MANUAL) ){
                        plotBandmode.setFrom(data[0]);
                        modePlotLineList.add(PlotLine.builder().text("MANUAL").value(data[0]).build() );
                    }
                    if (verifyMode.equals(ModeRobot.AUTO) ){
                        plotBandmode.setTo(data[0]);
                        modePlotLineList.add(PlotLine.builder().text("AUTO").value(data[0]).build() );
                        modePlotBandList.add(PlotBand.builder()
                                .from(plotBandmode.getFrom())
                                .to(plotBandmode.getTo())
                                .text(plotBandmode.getText())
                                .build());
                    }
                    break;
                case CONNECTION:
                    data[5] = Connection.parseValue(property.getValue());
                    connectionList.add(new Object[]{data[0], data[5]});
                    if (!verifyCNX.equals(Connection.valueOf(property.getValue()))) {
                        verifyCNX = Connection.valueOf(property.getValue());
                    }
                    if (verifyCNX.equals(Connection.DISCONNECTED) ){
                        plotBandCnx.setFrom(data[0]);


                        connectionPlotLineList.add(PlotLine.builder().text("DISCONNECTED").value(data[0]).build() );

                    }
                    if (verifyCNX.equals(Connection.CONNECTED) ){
                        plotBandCnx.setTo(data[0]);
                        connectionPlotLineList.add(PlotLine.builder().text("CONNECTED").value(data[0]).build() );

                        connectionPlotBandList.add(PlotBand.builder()
                                .from(plotBandCnx.getFrom())
                                .to(plotBandCnx.getTo())
                                .text(plotBandCnx.getText())
                                .build());
                     }
                    break;



                case SPEED:
                    data[1] = parseDoubleValue(property.getValue());
                    speedList.add(new Object[]{data[0], data[1]});
                    break;
                case LEVEL_BATTERY:
                    data[2] = parseDoubleValue(property.getValue());
                    batteryList.add(new Object[]{data[0], data[2]});
                    break;
                case STATUS_ROBOT:
                    data[3] = StatusRobot.parseValue(property.getValue());
                    statusList.add(new Object[]{data[0], data[3]});
                    break;
                case OPERATION_STATUS:
                    data[4] = OperationStatus.parseValue(property.getValue());
                    operationStatusList.add(new Object[]{data[0], data[4]});
                    break;
            }
        }
        if (!properties.isEmpty()) {
            speedList.add(new Object[]{data[0], data[1]});
            batteryList.add(new Object[]{data[0], data[2]});
            statusList.add(new Object[]{data[0], data[3]});
            operationStatusList.add(new Object[]{data[0], data[4]});
            if (verifyCNX.equals(Connection.DISCONNECTED) ){
                plotBandCnx.setTo(data[0]);
                connectionPlotBandList.add(PlotBand.builder()
                        .from(plotBandCnx.getFrom())
                        .to(plotBandCnx.getTo())
                        .text(plotBandCnx.getText())
                        .build());
            }
            if (verifyMode.equals(ModeRobot.MANUAL) ){
                plotBandmode.setTo(data[0]);
                modePlotBandList.add(PlotBand.builder()
                        .from(plotBandmode.getFrom())
                        .to(plotBandmode.getTo())
                        .text(plotBandmode.getText())
                        .build());
            }
        }
        Object[][] speedArray = speedList.toArray(new Object[0][]);
        Object[][] batteryArray = batteryList.toArray(new Object[0][]);
        Object[][] statusArray = statusList.toArray(new Object[0][]);
        Object[][] operationStatusArray = operationStatusList.toArray(new Object[0][]);
        PlotBand[] connectionPlotBandsArray = connectionPlotBandList.toArray(new PlotBand[0]);
        PlotBand[] modenPlotBandsArray = modePlotBandList.toArray(new PlotBand[0]);
        PlotLine[] connectionPlotLinesArray = connectionPlotLineList.toArray(new PlotLine[0]);
        PlotLine[] modePlotLineArray = modePlotLineList.toArray(new PlotLine[0]);
        return RobotDataChart.builder()
                .name(name)
                .speed(speedArray)
                .battery(batteryArray)
                .statusRobot(statusArray)
                .operationStatus(operationStatusArray)
                .connectionPlotBand(connectionPlotBandsArray)
                .modePlotBand(modenPlotBandsArray)
                .connectionPlotLine(connectionPlotLinesArray)
                .modePlotLine(modePlotLineArray)
                .build();
    }

    private static double parseDoubleValue(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
    private static Connection parseConnection(String str) {
        try {
            return  Connection.valueOf(str);
        } catch (IllegalArgumentException e) {
            return  Connection.DISCONNECTED;
        }
    }








    public static RobotDataBand mapToRobotDataBand(String name, List<RobotProperty> properties) {
        List<PlotBand> listDesconnected = new ArrayList<PlotBand>();
        List<PlotBand> listConnected= new ArrayList<PlotBand>();
        List<PlotBand> listManual= new ArrayList<PlotBand>();
        List<PlotBand> listAuto= new ArrayList<PlotBand>();
        List<PlotBand> listNormal= new ArrayList<PlotBand>();
        List<PlotBand> listEms= new ArrayList<PlotBand>();
        List<PlotBand> listPause= new ArrayList<PlotBand>();
        List<PlotBand> listInactive= new ArrayList<PlotBand>();
        List<PlotBand> listWaiting= new ArrayList<PlotBand>();
        List<PlotBand> listRunning= new ArrayList<PlotBand>();
        List<PlotBand> listCharge= new ArrayList<PlotBand>();
        List<PlotBand> listDischarge= new ArrayList<PlotBand>();
        List<PlotBand> listMaxSpeed= new ArrayList<PlotBand>();
        List<PlotBand> listMinSpeed= new ArrayList<PlotBand>();
        List<PlotBand> listNormalSpeed= new ArrayList<PlotBand>();

        List<PlotBand> objj = new ArrayList<PlotBand>();

        PlotBand plotDesconnected = PlotBand.builder().from(Connection.DISCONNECTED).text("Desconnected").build();
        long dateNow = new Date().getTime();
        Connection verifyDISCONNECTED = Connection.DISCONNECTED;
        for (RobotProperty property : properties) {
            dateNow = property.getTimestamp().getTime();
            switch (property.getType()) {
                case CONNECTION:
                    if (!verifyDISCONNECTED.equals(Connection.valueOf(property.getValue()))) {
                        verifyDISCONNECTED = Connection.valueOf(property.getValue());
                    }
                    if (verifyDISCONNECTED.equals(Connection.DISCONNECTED) ){
                        plotDesconnected.setFrom(dateNow);
                    }
                    if (verifyDISCONNECTED.equals(Connection.CONNECTED) ){
                        plotDesconnected.setTo(dateNow);
                        listDesconnected.add(
                                PlotBand.builder()
                                .from(plotDesconnected.getFrom())
                                .to(plotDesconnected.getTo())
                                .text(plotDesconnected.getText())
                                .build()
                        );
                    }
                    break;
            }
        }



        if (!properties.isEmpty()) {
            if (verifyDISCONNECTED.equals(Connection.DISCONNECTED) ){
                plotDesconnected.setTo(dateNow);
                listDesconnected.add(PlotBand.builder()
                        .from(plotDesconnected.getFrom())
                        .to(plotDesconnected.getTo())
                        .text(plotDesconnected.getText())
                        .build());
            }
        }

        return new RobotDataBand(name, listDesconnected,listConnected,listManual,listAuto,listNormal,listEms,listPause,listInactive,listWaiting,listRunning,listCharge,listDischarge,listMaxSpeed,listMinSpeed,listNormalSpeed);
    }







}





            /*if (property.getType().equals(TypeProperty.CONNECTION)) {
                data[5] = Connection.parseValue(property.getValue());
                connectionList.add(new Object[]{data[0], data[5]});

                verifyCNX = parseConnection(property.getValue());
                if ( verifyCNX.equals(Connection.DISCONNECTED )){
                    speedList.add(new Object[]{data[0], null});
                    batteryList.add(new Object[]{data[0], null});
                    statusList.add(new Object[]{data[0], null});
                    operationStatusList.add(new Object[]{data[0], null});}
                else if ( verifyCNX.equals(Connection.CONNECTED )) {
                    speedList.add(new Object[]{data[0], data[1]});
                    batteryList.add(new Object[]{data[0], data[2]});
                    statusList.add(new Object[]{data[0], data[3]});
                    operationStatusList.add(new Object[]{data[0], data[4]});}
            }

            if (verifyCNX.equals(Connection.DISCONNECTED)) {
                continue; }

            if (property.getType().equals(TypeProperty.SPEED)) {
                data[1] = parseDoubleValue(property.getValue());
                speedList.add(new Object[]{data[0], data[1]});
            }
            if (property.getType().equals(TypeProperty.LEVEL_BATTERY)) {
                data[2] = parseDoubleValue(property.getValue());
                batteryList.add(new Object[]{data[0], data[2]});
            }
            if (property.getType().equals(TypeProperty.STATUS_ROBOT)) {
                data[3] = StatusRobot.parseValue(property.getValue());
                statusList.add(new Object[]{data[0], data[3]});
            }
            if (property.getType().equals(TypeProperty.STATUS_ROBOT)) {
                data[4] = OperationStatus.parseValue(property.getValue());
                operationStatusList.add(new Object[]{data[0], data[4]});
            }*/