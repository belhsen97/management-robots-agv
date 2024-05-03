package com.enova.web.api.Mappers;

import com.enova.web.api.Enums.Connection;
import com.enova.web.api.Enums.ModeRobot;
import com.enova.web.api.Enums.OperationStatus;
import com.enova.web.api.Enums.StatusRobot;
import com.enova.web.api.Models.Entitys.RobotProperty;
import com.enova.web.api.Models.Responses.PlotBand;
import com.enova.web.api.Models.Responses.PlotLine;
import com.enova.web.api.Models.Responses.RobotDataBand;
import com.enova.web.api.Models.Responses.RobotDataChart;

import java.util.*;
import java.util.stream.Collectors;

public class RobotPropertyMapper {
    private static double parseDoubleValue(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
    public static  Map<String, List<RobotProperty>>  maptoPropertys  (List<RobotProperty> properties) {
        Map<String, List<RobotProperty>> robotPropertyMap  = new HashMap<String, List<RobotProperty>>();
        robotPropertyMap.put("Connection", new ArrayList<RobotProperty>());
        robotPropertyMap.put("ModeRobot", new ArrayList<RobotProperty>());
        robotPropertyMap.put("OperationStatus", new ArrayList<RobotProperty>());
        robotPropertyMap.put("StatusRobot", new ArrayList<RobotProperty>());
        robotPropertyMap.put("StatusBattery", new ArrayList<RobotProperty>());
        robotPropertyMap.put("StatusSpeed", new ArrayList<RobotProperty>());
        for (RobotProperty property : properties) {
            switch (property.getType()) {
                case CONNECTION:
                    robotPropertyMap.get("Connection").add(property);
                    break;
                case MODE_ROBOT:
                    robotPropertyMap.get("ModeRobot").add(property);
                    break;
                case OPERATION_STATUS:
                    robotPropertyMap.get("OperationStatus").add(property);
                    break;
                case STATUS_ROBOT:
                    robotPropertyMap.get("StatusRobot").add(property);
                    break;
                case LEVEL_BATTERY:
                    robotPropertyMap.get("StatusBattery").add(property);
                    break;
                case SPEED:
                    robotPropertyMap.get("StatusSpeed").add(property);
                    break;
            }
        }
        return robotPropertyMap;
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
        if (  properties.isEmpty()){return new RobotDataChart();}
        properties = properties.stream()
                .sorted(Comparator.comparing(RobotProperty::getTimestamp))
                .collect(Collectors.toList());
        Long lastTimestamp = (properties.get(properties.size() - 1).getTimestamp().getTime());

        Map<String, List<RobotProperty>> robotPropertyMap  =  maptoPropertys(properties);


        List<Object[]> speedList = new ArrayList<>();
        List<Object[]> batteryList = new ArrayList<>();
        List<Object[]> statusList = new ArrayList<>();
        List<Object[]> operationStatusList = new ArrayList<>();


        Long timestamp = null;
        for (RobotProperty property : robotPropertyMap.get("StatusSpeed")) {
            timestamp = property.getTimestamp().getTime();
            final Double speedValue = parseDoubleValue(property.getValue());
            speedList.add(new Object[]{timestamp, speedValue});
        }
        timestamp = null;
        for (RobotProperty property : robotPropertyMap.get("StatusBattery")) {
            timestamp = property.getTimestamp().getTime();
            final Double batteryValue = parseDoubleValue(property.getValue());
            batteryList.add(new Object[]{timestamp, batteryValue});
        }
        Integer valueStatusRobot = null;
        timestamp = null;
        for (RobotProperty property : robotPropertyMap.get("StatusRobot")) {
            timestamp = property.getTimestamp().getTime();
            valueStatusRobot = StatusRobot.parseValue(property.getValue());
            statusList.add(new Object[]{timestamp, valueStatusRobot});
        }
        Integer valueOperationStatus = null;
        timestamp = null;
        for (RobotProperty property : robotPropertyMap.get("OperationStatus")) {
            timestamp = property.getTimestamp().getTime();

            valueOperationStatus = OperationStatus.parseValue(property.getValue());
            operationStatusList.add(new Object[]{timestamp, valueOperationStatus});
        }





        List<PlotLine> connectionPlotLineList = new ArrayList<>();
        List<PlotBand> connectionPlotBandList = new ArrayList<>();
        timestamp = null;
        Long previousTimestamp = null;
        Connection currentConnection  = null;
        for (RobotProperty property : robotPropertyMap.get("Connection")) {
            timestamp =  property.getTimestamp().getTime();
            final Connection connection = Connection.valueOf(property.getValue());
            if (currentConnection == null) {
                currentConnection = connection;
                previousTimestamp = timestamp;
                continue; }

            if (!connection.equals(currentConnection)) {
                connectionPlotLineList.add(PlotLine.builder().text(connection.name()).value(timestamp).build() );
                connectionPlotBandList.add(
                        PlotBand.builder()
                                .from(previousTimestamp)
                                .to(timestamp)
                                .text(currentConnection.name())
                                .build()
                );
                currentConnection = connection;
                previousTimestamp = timestamp;
            }
        }
        if (currentConnection != null) {
            connectionPlotBandList.add(
                    PlotBand.builder()
                            .from(previousTimestamp)
                            .to(lastTimestamp)
                            .text(currentConnection.name())
                            .build()
            );
        }




        List<PlotBand> modePlotBandList = new ArrayList<>();
        List<PlotLine> modePlotLineList = new ArrayList<>();
        timestamp = null;
        previousTimestamp = null;
        ModeRobot currentModeRobot  = null;
        for (RobotProperty property : robotPropertyMap.get("ModeRobot")) {
            timestamp =  property.getTimestamp().getTime();
            final ModeRobot connection = ModeRobot.valueOf(property.getValue());
            if (currentModeRobot == null) {
                currentModeRobot = connection;
                previousTimestamp = timestamp;
                continue; }

            if (!connection.equals(currentModeRobot)) {
                modePlotLineList.add(PlotLine.builder().text(connection.name()).value(timestamp).build() );
                modePlotBandList.add(
                        PlotBand.builder()
                                .from(previousTimestamp)
                                .to(timestamp)
                                .text(currentModeRobot.name())
                                .build()
                );
                currentModeRobot = connection;
                previousTimestamp = timestamp;
            }
        }
        if (currentModeRobot != null) {
            modePlotBandList.add(
                    PlotBand.builder()
                            .from(previousTimestamp)
                            .to(lastTimestamp)
                            .text(currentModeRobot.name())
                            .build()
            );
        }



        Object[][] speedArray = speedList.toArray(new Object[0][]);
        Object[][] batteryArray = batteryList.toArray(new Object[0][]);

        if (!robotPropertyMap.get("StatusRobot").isEmpty()) {
            statusList.add(new Object[]{lastTimestamp, valueStatusRobot});
        }
        Object[][] statusArray = statusList.toArray(new Object[0][]);
        if (!robotPropertyMap.get("OperationStatus").isEmpty()) {
            operationStatusList.add(new Object[]{lastTimestamp, valueOperationStatus});
        }
        Object[][] operationStatusArray = operationStatusList.toArray(new Object[0][]);



        connectionPlotBandList = connectionPlotBandList.stream()
                .filter(plotBand -> Connection.DISCONNECTED.name().equals(plotBand.getText()))
                .collect(Collectors.toList());
        modePlotBandList = modePlotBandList.stream()
                .filter(plotBand -> ModeRobot.AUTO.name().equals(plotBand.getText()))
                .collect(Collectors.toList());


        PlotBand[] connectionPlotBandsArray = connectionPlotBandList.toArray(new PlotBand[0]);
        PlotLine[] connectionPlotLinesArray = connectionPlotLineList.toArray(new PlotLine[0]);
        PlotBand[] modePlotBandsArray = modePlotBandList.toArray(new PlotBand[0]);
        PlotLine[] modePlotLineArray = modePlotLineList.toArray(new PlotLine[0]);

        return RobotDataChart.builder()
                .name(name)
                .speed(speedArray)
                .battery(batteryArray)
                .statusRobot(statusArray)
                .operationStatus(operationStatusArray)
                .connectionPlotBand(connectionPlotBandsArray)
                .modePlotBand(modePlotBandsArray)
                .connectionPlotLine(connectionPlotLinesArray)
                .modePlotLine(modePlotLineArray)
                .build();
    }











    public static RobotDataBand mapToRobotDataBand(String name, List<RobotProperty> properties) {
        //Arrays.asList();
        final String CHARGED = "CHARGED"; final String DISCHARGED = "DISCHARGED";
        final String STANDBY = "STANDBY";final String MIN = "MIN"; final String MAX = "MAX";

        final Double min = 7.0; final Double max = 8.0;


        properties = properties.stream()
                .sorted(Comparator.comparing(RobotProperty::getTimestamp))
                .collect(Collectors.toList());


        Map<String, List<RobotProperty>> robotPropertyMap  =  maptoPropertys(properties);


        Map<String, List<PlotBand>> plotMap  = new HashMap<String, List<PlotBand>>();
        plotMap.put("Connection", new ArrayList<PlotBand>());
        plotMap.put("ModeRobot", new ArrayList<PlotBand>());
        plotMap.put("OperationStatus", new ArrayList<PlotBand>());
        plotMap.put("StatusRobot", new ArrayList<PlotBand>());
        plotMap.put("StatusBattery", new ArrayList<PlotBand>());
        plotMap.put("StatusSpeed", new ArrayList<PlotBand>());

        long timestamp = 0L;

        Long previousTimestamp = null;
        Connection currentConnection  = null;
        for (RobotProperty property : robotPropertyMap.get("Connection")) {
            timestamp =  property.getTimestamp().getTime();
            final Connection connection = Connection.valueOf(property.getValue());
            if (currentConnection == null) {
                currentConnection = connection;
                previousTimestamp = timestamp;
                continue; }
            if (!connection.equals(currentConnection)) {
                plotMap.get("Connection").add(
                        PlotBand.builder()
                                .from(previousTimestamp)
                                .to(timestamp)
                                .text(currentConnection.name())
                                .build()
                );
                currentConnection = connection;
                previousTimestamp = timestamp;
            }
        }
        if (currentConnection != null) {
            plotMap.get("Connection").add(
                    PlotBand.builder()
                            .from(previousTimestamp)
                            .to(System.currentTimeMillis())
                            .text(currentConnection.name())
                            .build()
            );
        }

        previousTimestamp = null;
        ModeRobot currentModeRobot = null;
        for (RobotProperty property : robotPropertyMap.get("ModeRobot")) {
            timestamp =  property.getTimestamp().getTime();
            final ModeRobot mode = ModeRobot.valueOf(property.getValue());
            if (currentModeRobot == null) {
                currentModeRobot = mode;
                previousTimestamp = timestamp;
                continue;
            }
            if (!mode.equals(currentModeRobot)) {
                plotMap.get("ModeRobot").add(
                        PlotBand.builder()
                                .from(previousTimestamp)
                                .to(timestamp)
                                .text(currentModeRobot.name())
                                .build());
                currentModeRobot = mode;
                previousTimestamp = timestamp;
            }
        }
        if (currentModeRobot != null) {
            plotMap.get("ModeRobot").add(
                    PlotBand.builder()
                            .from(previousTimestamp)
                            .to(System.currentTimeMillis())
                            .text(currentModeRobot.name())
                            .build()
            );
        }

        previousTimestamp = null;
        OperationStatus currentOperationStatus  = null;
        for (RobotProperty property : robotPropertyMap.get("OperationStatus")) {
            timestamp =  property.getTimestamp().getTime();
            final OperationStatus operationStatusRobot = OperationStatus.valueOf(property.getValue());
            if (currentOperationStatus == null) {
                currentOperationStatus = operationStatusRobot;
                previousTimestamp = timestamp;
                continue;
            }
            if (!operationStatusRobot.equals(currentOperationStatus)) {
                plotMap.get("OperationStatus").add(
                        PlotBand.builder()
                                .from(previousTimestamp)
                                .to(timestamp)
                                .text(currentOperationStatus.name())
                                .build()
                );
                currentOperationStatus = operationStatusRobot;
                previousTimestamp = timestamp;
            }
        }
        if (currentOperationStatus != null) {
            plotMap.get("OperationStatus").add(
                    PlotBand.builder()
                            .from(previousTimestamp)
                            .to(System.currentTimeMillis())
                            .text(currentOperationStatus.name())
                            .build()
            );
        }

        previousTimestamp = null;
        StatusRobot currentStatusRobot = null;
        for (RobotProperty property : robotPropertyMap.get("StatusRobot")) {
            timestamp =  property.getTimestamp().getTime();
            final StatusRobot statusRobot = StatusRobot.valueOf(property.getValue());
            if (currentStatusRobot == null) {
                currentStatusRobot = statusRobot;
                previousTimestamp = timestamp;
                continue;
            }
            if (!statusRobot.equals(currentStatusRobot)) {
                plotMap.get("StatusRobot").add(
                        PlotBand.builder()
                                .from(previousTimestamp)
                                .to(timestamp)
                                .text(currentStatusRobot.name())
                                .build()
                );
                currentStatusRobot = statusRobot;
                previousTimestamp = timestamp;
            }
        }
        if (currentStatusRobot != null) {
            plotMap.get("StatusRobot").add(
                    PlotBand.builder()
                            .from(previousTimestamp)
                            .to(System.currentTimeMillis())
                            .text(currentStatusRobot.name())
                            .build()
            );
        }

        previousTimestamp = null;
        Double  previousTlevelBattery = null;
        String currentStatusBattery = null;
        for (RobotProperty property : robotPropertyMap.get("StatusBattery")) {
            timestamp =  property.getTimestamp().getTime();
            final double levelBattery = parseDoubleValue(property.getValue());
            if (previousTlevelBattery == null) {
                previousTlevelBattery = levelBattery;
                previousTimestamp = timestamp;
                continue;
            }
            String statusBattery = null;
            if (previousTimestamp != timestamp) {
                if (previousTlevelBattery < levelBattery) {
                    statusBattery = CHARGED;
                } else if (previousTlevelBattery > levelBattery) {
                    statusBattery = DISCHARGED;
                } else if (previousTlevelBattery == levelBattery) {
                    statusBattery = STANDBY;
                }
                previousTlevelBattery = levelBattery;
            }
            //if (statusBattery == null) { continue; }
            if (currentStatusBattery == null || statusBattery == null) {
                currentStatusBattery = statusBattery; continue; }

            if (!statusBattery.equals(currentStatusBattery)) {
                plotMap.get("StatusBattery").add(
                        PlotBand.builder()
                                .from(previousTimestamp)
                                .to(timestamp)
                                .text(statusBattery)
                                .build());
                previousTimestamp = timestamp;
                currentStatusBattery = statusBattery;
            }

        }

        if (currentStatusBattery != null) {
            plotMap.get("StatusBattery").add(
                    PlotBand.builder()
                            .from( previousTimestamp )
                            .to(timestamp)
                            .text(currentStatusBattery)
                            .build()
            );
        }




        previousTimestamp = null;
        String currentStatusSpeed = null;
        for (RobotProperty property : robotPropertyMap.get("StatusSpeed")) {
            timestamp =  property.getTimestamp().getTime();
            final double speedValue = parseDoubleValue(property.getValue());
            if (previousTimestamp == null) {
                previousTimestamp = timestamp;
                continue;
            }
            String statusSpeed = null;
            if (previousTimestamp != timestamp) {
                if (speedValue > max) {
                    statusSpeed = MAX;
                } else if (speedValue < min) {
                    statusSpeed = MIN;
                } else if (speedValue <= max && speedValue >= max) {
                    statusSpeed = STANDBY;
                }
            }
            //if (statusSpeed == null) { continue; }
            if (currentStatusSpeed == null || statusSpeed == null) {
                currentStatusSpeed = statusSpeed; continue; }

            if (!statusSpeed.equals(currentStatusSpeed)) {
                plotMap.get("StatusSpeed").add(
                        PlotBand.builder()
                                .from(previousTimestamp)
                                .to(timestamp)
                                .text(statusSpeed)
                                .build());
                previousTimestamp = timestamp;
                currentStatusSpeed = statusSpeed;
            }

        }

        if (currentStatusSpeed != null) {
            plotMap.get("StatusSpeed").add(
                    PlotBand.builder()
                            .from( previousTimestamp )
                            .to(timestamp)
                            .text(currentStatusSpeed)
                            .build()
            );
        }


        Map<String, List<PlotBand>> plotMapOut  = new HashMap<String, List<PlotBand>>();
        plotMapOut.put("Desconnected", new ArrayList<PlotBand>());
        plotMapOut.put("Connected", new ArrayList<PlotBand>());
        plotMapOut.put("Manual", new ArrayList<PlotBand>());
        plotMapOut.put("Auto", new ArrayList<PlotBand>());
        plotMapOut.put("Normal", new ArrayList<PlotBand>());
        plotMapOut.put("Ems", new ArrayList<PlotBand>());
        plotMapOut.put("Pause", new ArrayList<PlotBand>());
        plotMapOut.put("Inactive", new ArrayList<PlotBand>());
        plotMapOut.put("Waiting", new ArrayList<PlotBand>());
        plotMapOut.put("Running", new ArrayList<PlotBand>());
        plotMapOut.put("Charge", new ArrayList<PlotBand>());
        plotMapOut.put("Discharge", new ArrayList<PlotBand>());
        plotMapOut.put("Standby", new ArrayList<PlotBand>());
        plotMapOut.put("MaxSpeed", new ArrayList<PlotBand>());
        plotMapOut.put("MinSpeed", new ArrayList<PlotBand>());
        plotMapOut.put("NormalSpeed", new ArrayList<PlotBand>());

        plotMap.get("Connection").stream().forEach(plotBand -> {
            if (Connection.CONNECTED.name().equals(plotBand.getText())) { plotMap.get("Desconnected").add(plotBand);   }
            else if (Connection.DISCONNECTED.name().equals(plotBand.getText())) {   plotMap.get("Connected").add(plotBand); }
        });
        plotMap.get("ModeRobot").stream().forEach(plotBand -> {
            if (ModeRobot.MANUAL.name().equals(plotBand.getText())) {     plotMap.get("Manual").add(plotBand);   }
            else if (ModeRobot.AUTO.name().equals(plotBand.getText())) {  plotMap.get("Auto") .add(plotBand); }
        });
        plotMap.get("OperationStatus").stream().forEach(plotBand -> {
            if (OperationStatus.NORMAL.name().equals(plotBand.getText())) {     plotMap.get("Normal").add(plotBand); }
            else if (OperationStatus.EMS.name().equals(plotBand.getText())) {   plotMap.get("Ems").add(plotBand); }
            else if (OperationStatus.PAUSE.name().equals(plotBand.getText())) {    plotMap.get("Pause").add(plotBand); }
        });
        plotMap.get("StatusRobot").stream().forEach(plotBand -> {
            if (StatusRobot.INACTIVE.name().equals(plotBand.getText())) { plotMap.get("Inactive").add(plotBand); }
            else if (StatusRobot.WAITING.name().equals(plotBand.getText())) {   plotMap.get("Waiting").add(plotBand); }
            else if (StatusRobot.RUNNING.name().equals(plotBand.getText())) {  plotMap.get("Running").add(plotBand); }
        });
        plotMap.get("StatusBattery").stream().forEach(plotBand -> {
            if (CHARGED.equals(plotBand.getText())) { plotMap.get("Charge").add(plotBand); }
            else if (DISCHARGED.equals(plotBand.getText())) { plotMap.get("Discharge").add(plotBand); }
            else if (STANDBY.equals(plotBand.getText())) {   plotMap.get("Standby").add(plotBand); }
        });
        plotMap.get("StatusSpeed").stream().forEach(plotBand -> {
            if (MIN.equals(plotBand.getText())) {  plotMap.get("MinSpeed").add(plotBand); }
            else if (MAX.equals(plotBand.getText())) {  plotMap.get("MaxSpeed").add(plotBand); }
            else if (STANDBY.equals(plotBand.getText())) { plotMap.get("NormalSpeed") .add(plotBand); }
        });

//        for (PlotBand plot : listStandby ){
//            String stringToConvert = String.valueOf(plot.getFrom());
//            Long start = Long.parseLong(stringToConvert);
//            stringToConvert = String.valueOf(plot.getTo());
//            Long end = Long.parseLong(stringToConvert);
//
//            System.out.println("start : " + new Date(start).toString() +"- end"+new Date(end).toString());
//            System.out.println(plot.toString());
//        }

        return new RobotDataBand(name,
                plotMap.get("Desconnected"),
                plotMap.get("Connected"),
                plotMap.get("Manual"),
                plotMap.get("Auto"),
                plotMap.get("Normal"),
                plotMap.get("Ems"),
                plotMap.get("Pause"),
                plotMap.get("Inactive"),
                plotMap.get("Waiting"),
                plotMap.get("Running"),
                plotMap.get("Charge"),
                plotMap.get("Discharge"),
                plotMap.get("Standby"),
                plotMap.get("MaxSpeed"),
                plotMap.get("MinSpeed"),
                plotMap.get("NormalSpeed"));
    }
}
