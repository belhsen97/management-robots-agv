package tn.enova.Mappers;

import tn.enova.Enums.Connection;
import tn.enova.Enums.ModeRobot;
import tn.enova.Enums.OperationStatus;
import tn.enova.Enums.StatusRobot;
import tn.enova.Models.Dtos.RobotSettingDto;
import tn.enova.Models.Entitys.RobotProperty;
import tn.enova.Models.Responses.PlotBand;
import tn.enova.Models.Responses.PlotLine;
import tn.enova.Models.Responses.RobotDataBand;
import tn.enova.Models.Responses.RobotDataChart;

import java.util.*;
import java.util.stream.Collectors;

public class RobotPropertyMapper {
    public static RobotSettingDto globalSetting = RobotSettingDto.builder()
            .speed(new HashMap<String, Double>() {{
                put("min", 7.0D);
                put("max", 8.0D);
            }})
            .distance(new HashMap<String, Double>() {{
                put("min", 4D);
                put("max", 10D);
            }})
            .build();

    private static double parseDoubleValue(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public static Map<String, List<RobotProperty>> maptoPropertys(List<RobotProperty> properties) {
        Map<String, List<RobotProperty>> robotPropertyMap = new HashMap<String, List<RobotProperty>>();
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


    public static List<RobotDataChart> mapToRobotsDataChart(List<RobotProperty> properties) {
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
            robotDataList.add(mapToRobotDataChart(name, props));
        }

        return robotDataList;
    }


    public static RobotDataChart mapToRobotDataChart(String name, List<RobotProperty> properties) {
        if (properties.isEmpty()) {
            return new RobotDataChart();
        }
        properties = properties.stream()
                .sorted(Comparator.comparing(RobotProperty::getTimestamp))
                .collect(Collectors.toList());
        Long lastTimestamp = (properties.get(properties.size() - 1).getTimestamp().getTime());

        Map<String, List<RobotProperty>> robotPropertyMap = maptoPropertys(properties);


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
        Connection currentConnection = null;
        for (RobotProperty property : robotPropertyMap.get("Connection")) {
            timestamp = property.getTimestamp().getTime();
            final Connection connection = Connection.valueOf(property.getValue());
            if (currentConnection == null) {
                currentConnection = connection;
                previousTimestamp = timestamp;
                continue;
            }

            if (!connection.equals(currentConnection)) {
                connectionPlotLineList.add(PlotLine.builder().text(connection.name()).value(timestamp).build());
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
        ModeRobot currentModeRobot = null;
        for (RobotProperty property : robotPropertyMap.get("ModeRobot")) {
            timestamp = property.getTimestamp().getTime();
            final ModeRobot connection = ModeRobot.valueOf(property.getValue());
            if (currentModeRobot == null) {
                currentModeRobot = connection;
                previousTimestamp = timestamp;
                continue;
            }

            if (!connection.equals(currentModeRobot)) {
                modePlotLineList.add(PlotLine.builder().text(connection.name()).value(timestamp).build());
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

    public static List<RobotDataBand> mapToRobotsDataBand(List<RobotProperty> properties) {
        if (properties.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<RobotProperty>> propertyMap = new HashMap<>();
        for (RobotProperty property : properties) {
            propertyMap.computeIfAbsent(property.getName(), k -> new ArrayList<>()).add(property);
        }

        List<RobotDataBand> robotDataList = new ArrayList<>();
        for (Map.Entry<String, List<RobotProperty>> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            List<RobotProperty> props = entry.getValue();
            robotDataList.add(mapToRobotDataBand(name, props));
        }
        robotDataList = robotDataList.stream()
                .sorted(Comparator.comparing(RobotDataBand::getName))
                .collect(Collectors.toList());

        return robotDataList;
    }
    public static RobotDataBand mapToRobotDataBand(String name, List<RobotProperty> properties) {
        //Arrays.asList();
        final String CHARGED = "CHARGED";
        final String DISCHARGED = "DISCHARGED";
        final String STANDBY = "STANDBY";
        final String MIN = "MIN";
        final String MAX = "MAX";


        properties = properties.stream()
                .sorted(Comparator.comparing(RobotProperty::getTimestamp))
                .collect(Collectors.toList());


        Map<String, List<RobotProperty>> robotPropertyMap = maptoPropertys(properties);


        Map<String, List<PlotBand>> plotMap = new HashMap<String, List<PlotBand>>();
        plotMap.put("Connection", new ArrayList<PlotBand>());
        plotMap.put("ModeRobot", new ArrayList<PlotBand>());
        plotMap.put("OperationStatus", new ArrayList<PlotBand>());
        plotMap.put("StatusRobot", new ArrayList<PlotBand>());
        plotMap.put("StatusBattery", new ArrayList<PlotBand>());
        plotMap.put("StatusSpeed", new ArrayList<PlotBand>());

        long timestamp = 0L;

        Long previousTimestamp = null;
        Connection currentConnection = null;
        for (RobotProperty property : robotPropertyMap.get("Connection")) {
            timestamp = property.getTimestamp().getTime();
            final Connection connection = Connection.valueOf(property.getValue());
            if (currentConnection == null) {
                currentConnection = connection;
                previousTimestamp = timestamp;
                continue;
            }
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
            timestamp = property.getTimestamp().getTime();
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
        OperationStatus currentOperationStatus = null;
        for (RobotProperty property : robotPropertyMap.get("OperationStatus")) {
            timestamp = property.getTimestamp().getTime();
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
            timestamp = property.getTimestamp().getTime();
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
        Double previousTlevelBattery = null;
        String currentStatusBattery = null;
        for (RobotProperty property : robotPropertyMap.get("StatusBattery")) {
            timestamp = property.getTimestamp().getTime();
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
                currentStatusBattery = statusBattery;
                continue;
            }

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
                            .from(previousTimestamp)
                            .to(timestamp)
                            .text(currentStatusBattery)
                            .build()
            );
        }


        previousTimestamp = null;
        String currentStatusSpeed = null;
        for (RobotProperty property : robotPropertyMap.get("StatusSpeed")) {
            timestamp = property.getTimestamp().getTime();
            final double speedValue = parseDoubleValue(property.getValue());
            if (previousTimestamp == null) {
                previousTimestamp = timestamp;
                continue;
            }
            String statusSpeed = null;
            if (previousTimestamp != timestamp) {
                if (speedValue > globalSetting.getSpeed().get("max")) {
                    statusSpeed = MAX;
                } else if (speedValue < globalSetting.getSpeed().get("min")) {
                    statusSpeed = MIN;
                } else if (speedValue <= globalSetting.getSpeed().get("max") && speedValue >= globalSetting.getSpeed().get("min")) {
                    statusSpeed = STANDBY;
                }
            }
            //if (statusSpeed == null) { continue; }
            if (currentStatusSpeed == null || statusSpeed == null) {
                currentStatusSpeed = statusSpeed;
                continue;
            }

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
                            .from(previousTimestamp)
                            .to(timestamp)
                            .text(currentStatusSpeed)
                            .build()
            );
        }


        Map<String, List<PlotBand>> plotMapOut = new HashMap<String, List<PlotBand>>();
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



        double incIntervalFirst = 0D;
        double incIntervalSecond = 0D;
        double incIntervalThird = 0D;
        double incFrequencyFirst = 0D;
        double incFrequencySecond = 0D;
        double incFrequencyThird = 0D;
        for (PlotBand plotBand : plotMap.get("Connection")) {
            if (Connection.CONNECTED.name().equals(plotBand.getText())) {
                plotMapOut.get("Connected").add(plotBand);
                incIntervalFirst += plotBand.getTo() - plotBand.getFrom();
                incFrequencyFirst += 1;
            } else if (Connection.DISCONNECTED.name().equals(plotBand.getText())) {
                plotMapOut.get("Desconnected").add(plotBand);
                incIntervalSecond   += plotBand.getTo() - plotBand.getFrom();
                incFrequencySecond += 1;
            }
        }

        Map<String, Double> durationConnection = new HashMap<String, Double>();
        durationConnection.put("connected", incIntervalFirst);
        durationConnection.put("desconnected", incIntervalSecond);
        Map<String, Double> frquencyConnection = new HashMap<String, Double>();
        frquencyConnection.put("connected", incFrequencyFirst);
        frquencyConnection.put("desconnected", incFrequencySecond);

        Map<String, Double> averageConnection = new HashMap<String, Double>();
        averageConnection.put("connected", new Double(((incIntervalFirst  * 100) /( incIntervalFirst +incIntervalSecond ))));
        averageConnection.put("desconnected", new Double(((incIntervalSecond  * 100) /( incIntervalFirst +incIntervalSecond ))));
        averageConnection.put("connected", (  averageConnection.get("connected").equals(Double.NaN)? 0:averageConnection.get("connected")) );
        averageConnection.put("desconnected", (  averageConnection.get("desconnected").equals(Double.NaN)? 0:averageConnection.get("desconnected")) );

        incIntervalFirst = 0D;
        incIntervalSecond = 0D;
        incFrequencyFirst = 0D;
        incFrequencySecond = 0D;
        for (PlotBand plotBand : plotMap.get("ModeRobot")) {
            if (ModeRobot.MANUAL.name().equals(plotBand.getText())) {
                plotMapOut.get("Manual").add(plotBand);
                incIntervalFirst += plotBand.getTo() - plotBand.getFrom();
                incFrequencyFirst += 1;
            } else if (ModeRobot.AUTO.name().equals(plotBand.getText())) {
                plotMapOut.get("Auto").add(plotBand);
                incIntervalSecond += plotBand.getTo() - plotBand.getFrom();
                incFrequencySecond += 1;
            }
        }



        Map<String, Double> durationStatusMode = new HashMap<String, Double>();
        durationStatusMode.put("manual",  incIntervalFirst );
        durationStatusMode.put("auto",  incIntervalSecond );
        Map<String, Double> frequencyStatusMode = new HashMap<String, Double>();
        frequencyStatusMode.put("manual",  incFrequencyFirst );
        frequencyStatusMode.put("auto",  incFrequencySecond );

        Map<String, Double> averageMode = new HashMap<String, Double>();
        averageMode.put("manual", new Double( ((incIntervalFirst  * 100) /( incIntervalFirst +incIntervalSecond ))));
        averageMode.put("auto", new Double(((incIntervalSecond  * 100) /( incIntervalFirst +incIntervalSecond ))));
        averageMode.put("manual", (  averageMode.get("manual").equals(Double.NaN)? 0:averageMode.get("manual")) );
        averageMode.put("auto", (  averageMode.get("auto").equals(Double.NaN)? 0:averageMode.get("auto")) );


        incIntervalFirst = 0D;
        incIntervalSecond = 0D;
        incFrequencyFirst = 0D;
        incFrequencySecond = 0D;
        for (PlotBand plotBand : plotMap.get("OperationStatus")) {
            if (OperationStatus.NORMAL.name().equals(plotBand.getText())) {
                plotMapOut.get("Normal").add(plotBand);
                incIntervalFirst += plotBand.getTo() - plotBand.getFrom();
                incFrequencyFirst += 1;
            } else if (OperationStatus.EMS.name().equals(plotBand.getText())) {
                plotMapOut.get("Ems").add(plotBand);
                incIntervalSecond += plotBand.getTo() - plotBand.getFrom();
                incFrequencySecond += 1;
            } else if (OperationStatus.PAUSE.name().equals(plotBand.getText())) {
                plotMapOut.get("Pause").add(plotBand);
                incIntervalThird += plotBand.getTo() - plotBand.getFrom();
                incFrequencyThird += 1;
            }
        }
        Map<String, Double> durationOperationStatus = new HashMap<String, Double>();
        durationOperationStatus.put("normal",  incIntervalFirst );
        durationOperationStatus.put("ems",  incIntervalSecond );
        durationOperationStatus.put("pause",  incIntervalThird);

        Map<String, Double> frequencyOperationStatus = new HashMap<String, Double>();
        frequencyOperationStatus.put("normal",  incFrequencyFirst );
        frequencyOperationStatus.put("ems",  incFrequencySecond );
        frequencyOperationStatus.put("pause",  incFrequencyThird);


        Map<String, Double> averageOperationStatus = new HashMap<String, Double>();
        averageOperationStatus.put("normal", new Double(    ((incIntervalFirst*100) /( incIntervalFirst+incIntervalSecond+incIntervalThird ))   ));
        averageOperationStatus.put("ems", new Double(   ((incIntervalSecond  * 100) /( incIntervalFirst+incIntervalSecond+incIntervalThird ))   ));
        averageOperationStatus.put("pause", new Double(   ((incIntervalThird  * 100) /( incIntervalFirst+incIntervalSecond+incIntervalThird ))   ));
        averageOperationStatus.put("normal", (  averageOperationStatus.get("normal").equals(Double.NaN)? 0:averageOperationStatus.get("normal")) );
        averageOperationStatus.put("ems", (  averageOperationStatus.get("ems").equals(Double.NaN)? 0:averageOperationStatus.get("ems")) );
        averageOperationStatus.put("pause", (  averageOperationStatus.get("pause").equals(Double.NaN)? 0:averageOperationStatus.get("pause")) );

        incIntervalFirst = 0D;
        incIntervalSecond = 0D;
        incIntervalThird = 0D;
        incFrequencyFirst = 0D;
        incFrequencySecond = 0D;
        incFrequencyThird = 0D;
        for (PlotBand plotBand : plotMap.get("StatusRobot")) {
            if (StatusRobot.INACTIVE.name().equals(plotBand.getText())) {
                plotMapOut.get("Inactive").add(plotBand);
                incIntervalFirst += plotBand.getTo() - plotBand.getFrom();
                incFrequencyFirst += 1;
            } else if (StatusRobot.WAITING.name().equals(plotBand.getText())) {
                plotMapOut.get("Waiting").add(plotBand);
                incIntervalSecond += plotBand.getTo() - plotBand.getFrom();
                incFrequencySecond += 1;
            } else if (StatusRobot.RUNNING.name().equals(plotBand.getText())) {
                plotMapOut.get("Running").add(plotBand);
                incIntervalThird += plotBand.getTo() - plotBand.getFrom();
                incFrequencyThird += 1;
            }
        }
        Map<String, Double> durationStatusRobot = new HashMap<String, Double>();
        durationStatusRobot.put("inactive",  incIntervalFirst );
        durationStatusRobot.put("waiting",  incIntervalSecond );
        durationStatusRobot.put("running",  incIntervalThird);

        Map<String, Double> frquencyStatusRobot = new HashMap<String, Double>();
        frquencyStatusRobot.put("inactive",  incFrequencyFirst );
        frquencyStatusRobot.put("waiting",  incFrequencySecond );
        frquencyStatusRobot.put("running",  incFrequencyThird);

        Map<String, Double> averageStatusRobot = new HashMap<String, Double>();
        averageStatusRobot.put("inactive", new Double(   ((incIntervalFirst * 100) /( incIntervalFirst+incIntervalSecond+incIntervalThird ))  ));
        averageStatusRobot.put("waiting", new Double(   ((incIntervalSecond  * 100) /( incIntervalFirst+incIntervalSecond+incIntervalThird ))  ));
        averageStatusRobot.put("running", new Double(  ((incIntervalThird  * 100) /( incIntervalFirst+incIntervalSecond+incIntervalThird ))  ));
        averageStatusRobot.put("inactive", (  averageStatusRobot.get("inactive").equals(Double.NaN)? 0:averageStatusRobot.get("inactive")) );
        averageStatusRobot.put("waiting", (  averageStatusRobot.get("waiting").equals(Double.NaN)? 0:averageStatusRobot.get("waiting")) );
        averageStatusRobot.put("running", (  averageStatusRobot.get("running").equals(Double.NaN)? 0:averageStatusRobot.get("running")) );



        incIntervalFirst= 0D;
        incIntervalSecond = 0D;
        incIntervalThird = 0D;
        incFrequencyFirst = 0D;
        incFrequencySecond = 0D;
        incFrequencyThird = 0D;
        for (PlotBand plotBand : plotMap.get("StatusBattery")) {
            if (CHARGED.equals(plotBand.getText())) {
                plotMapOut.get("Charge").add(plotBand);
                incIntervalFirst += plotBand.getTo() - plotBand.getFrom();
                incFrequencyFirst += 1;
            } else if (DISCHARGED.equals(plotBand.getText())) {
                plotMapOut.get("Discharge").add(plotBand);
                incIntervalSecond += plotBand.getTo() - plotBand.getFrom();
                incFrequencySecond += 1;
            } else if (STANDBY.equals(plotBand.getText())) {
                plotMapOut.get("Standby").add(plotBand);
                incIntervalThird += plotBand.getTo() - plotBand.getFrom();
                incFrequencyThird += 1;
            }
        }
        Map<String, Double> durationStatusBattery = new HashMap<String, Double>();
        durationStatusBattery.put("charge",  incIntervalFirst );
        durationStatusBattery.put("discharge",  incIntervalSecond );
        durationStatusBattery.put("standby",  incIntervalThird);

        Map<String, Double> frequencyStatusBattery = new HashMap<String, Double>();
        frequencyStatusBattery.put("charge",  incFrequencyFirst );
        frequencyStatusBattery.put("discharge",  incFrequencySecond );
        frequencyStatusBattery.put("standby",  incFrequencyThird);


        Map<String, Double> averageStatusBattery = new HashMap<String, Double>();
        averageStatusBattery.put("charge", new Double(   ((incIntervalFirst * 100) /( incIntervalFirst+incIntervalSecond+incIntervalThird )) ));
        averageStatusBattery.put("discharge", new Double(   ((incIntervalSecond  * 100) /( incIntervalFirst+incIntervalSecond+incIntervalThird )) ));
        averageStatusBattery.put("standby", new Double(  ((incIntervalThird  * 100) /( incIntervalFirst+incIntervalSecond+incIntervalThird )) ));
        averageStatusBattery.put("charge", (  averageStatusBattery.get("charge").equals(Double.NaN)? 0:averageStatusBattery.get("charge")) );
        averageStatusBattery.put("discharge", (  averageStatusBattery.get("discharge").equals(Double.NaN)? 0:averageStatusBattery.get("discharge")) );
        averageStatusBattery.put("standby", (  averageStatusBattery.get("standby").equals(Double.NaN)? 0:averageStatusBattery.get("standby")) );

        incIntervalFirst= 0D;
        incIntervalSecond = 0D;
        incIntervalThird = 0D;
        incFrequencyFirst = 0D;
        incFrequencySecond = 0D;
        incFrequencyThird = 0D;
        for (PlotBand plotBand : plotMap.get("StatusSpeed")) {
            if (MIN.equals(plotBand.getText())) {
                plotMapOut.get("MinSpeed").add(plotBand);
                incIntervalFirst += plotBand.getTo() - plotBand.getFrom();
                incFrequencyFirst += 1;
            } else if (MAX.equals(plotBand.getText())) {
                plotMapOut.get("MaxSpeed").add(plotBand);
                incIntervalSecond += plotBand.getTo() - plotBand.getFrom();
                incFrequencySecond += 1;
            } else if (STANDBY.equals(plotBand.getText())) {
                plotMapOut.get("NormalSpeed").add(plotBand);
                incIntervalThird += plotBand.getTo() - plotBand.getFrom();
                incFrequencyThird += 1;
            }
        }
        Map<String, Double> durationSpeed = new HashMap<String, Double>();
        durationSpeed.put("min",  incIntervalFirst );
        durationSpeed.put("max",  incIntervalSecond );
        durationSpeed.put("standby",  incIntervalThird);

        Map<String, Double> frequencySpeed = new HashMap<String, Double>();
        frequencySpeed.put("min",  incFrequencyFirst );
        frequencySpeed.put("max",  incFrequencySecond );
        frequencySpeed.put("standby",  incFrequencyThird);

        Map<String, Double> averageStatusSpeed = new HashMap<String, Double>();
        averageStatusSpeed.put("min", new Double(   ((incIntervalFirst * 100) /( incIntervalFirst+incIntervalSecond+incIntervalThird )) ));
        averageStatusSpeed.put("max", new Double(   ((incIntervalSecond  * 100) /( incIntervalFirst+incIntervalSecond+incIntervalThird )) ));
        averageStatusSpeed.put("standby", new Double(  ((incIntervalThird  * 100) /( incIntervalFirst+incIntervalSecond+incIntervalThird )) ));
        averageStatusSpeed.put("min", (  averageStatusSpeed.get("min").equals(Double.NaN)? 0:averageStatusSpeed.get("min")) );
        averageStatusSpeed.put("max", (  averageStatusSpeed.get("max").equals(Double.NaN)? 0:averageStatusSpeed.get("max")) );
        averageStatusSpeed.put("standby", (  averageStatusSpeed.get("standby").equals(Double.NaN)? 0:averageStatusSpeed.get("standby")) );



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
                new HashMap<String, Object>() {{
                    put("average",averageConnection);
                    put("duration",durationConnection);
                    put("frequency",frquencyConnection);
                    put("interval", new HashMap<String, Object>() {{
                                                                   put("connected", plotMapOut.get("Connected"));
                                                                  put("desconnected", plotMapOut.get("Desconnected"));
                                                                  }}
                    );
                }},
                new HashMap<String, Object>() {{
                    put("average",averageMode);
                    put("duration",durationStatusMode);
                    put("frequency",frequencyStatusMode);
                    put("interval", new HashMap<String, Object>() {{
                                put("manual", plotMapOut.get("Manual"));
                                put("auto", plotMapOut.get("Auto"));
                            }}
                    );
                }},
                new HashMap<String, Object>() {{
                    put("average",averageOperationStatus);
                    put("duration",durationOperationStatus);
                    put("frequency",frequencyOperationStatus);
                    put("interval", new HashMap<String, Object>() {{
                                put("normal", plotMapOut.get("Normal"));
                                put("ems", plotMapOut.get("Ems"));
                                put("pause", plotMapOut.get("Pause"));
                            }}
                    );
                }},
                new HashMap<String, Object>() {{
                    put("average",averageStatusRobot);
                    put("duration",durationStatusRobot);
                    put("frequency",frquencyStatusRobot);
                    put("interval", new HashMap<String, Object>() {{
                                put("inactive", plotMapOut.get("Inactive"));
                                put("waiting", plotMapOut.get("Waiting"));
                                put("running", plotMapOut.get("Running"));
                            }}
                    );
                }},
                new HashMap<String, Object>() {{
                    put("average",averageStatusBattery);
                    put("duration", durationStatusBattery);
                    put("frequency",frequencyStatusBattery);
                    put("interval", new HashMap<String, Object>() {{
                                put("charge", plotMapOut.get("Charge"));
                                put("discharge", plotMapOut.get("Discharge"));
                                put("standby", plotMapOut.get("Standby"));
                            }}
                    );
                }},
                new HashMap<String, Object>() {{
                    put("average",averageStatusSpeed);
                    put("duration", durationSpeed);
                    put("frequency",frequencySpeed);
                    put("interval", new HashMap<String, Object>() {{
                               put("max", plotMapOut.get("MaxSpeed"));
                               put("min", plotMapOut.get("MinSpeed"));
                               put("standby", plotMapOut.get("NormalSpeed"));
                            }}
                    );
                }}
        );
    }
}