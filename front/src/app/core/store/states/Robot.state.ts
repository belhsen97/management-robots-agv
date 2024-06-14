import { Connection } from '../models/Robot/Connection.enum';
import { ModeRobot } from '../models/Robot/ModeRobot.enum';
import { StatusRobot } from '../models/Robot/StatusRobot.enum';
import { OperationStatus } from '../models/Robot/OperationStatus.enum';
import { RobotDto } from '../models/Robot/RobotDto.model';
import { RobotSettingDto } from '../models/Robot/RobotSettingDto.model';
import { CountRobotsProperties } from '../models/Robot/CountRobotsProperties.model';
import { wsState } from './Worstation.state';
import { MatTableDataSource } from '@angular/material/table';
import { RobotDataChart } from '../models/Robot/RobotDataChart.model';
import { Plot, RobotDataBand } from '../models/Robot/RobotDataBand.model';

export interface RobotState {
    typeConnection?: ReadonlyArray<{ label: string; value: Connection }>;
    listModeRobot?: ReadonlyArray<{ label: string; value: ModeRobot }>;
    listOperationStatus?: ReadonlyArray<{ label: string; value: OperationStatus }>;
    listStatusRobot?: ReadonlyArray<{ label: string; value: StatusRobot }>;
    settingRobot?: RobotSettingDto;
    count?: CountRobotsProperties;
    dataSource?: MatTableDataSource<RobotDto>;
    robotDataChart:RobotDataChart;
    robot: RobotDto;
    robotDataBand?: RobotDataBand;
    listRobotDataBand?: RobotDataBand[];
    listRobotsData?:RobotDataChart[];
    listRobots: RobotDto[];
    errorMessage?:String;
}

const robotDataChart: RobotDataChart = {
    name: "robot-1",
    speed: [ [1712298068943,9.0]],
    battery: [ [1712298068943,9.0]],
    statusRobot:[[1712298362571,0]],
    operationStatus:[[1712298362571,0]],
    connectionPlotBand:[{from: 1647437400000, to: 1647523800000 ,text : "text" }],
    connectionPlotLine:[{value: 1647437400000,text : "Disconnected" }],
    modePlotBand:[{from: 1647437400000, to: 1647523800000 ,text : "text" }],
    modePlotLine:[{value: 1647437400000,text : "AUTO" }]
};
const robot: RobotDto = {
    id: "",
    clientid: "",
    username: "",
    password: "",
    createdAt: new Date(),
    name: "",
    statusRobot: StatusRobot.RUNNING,
    modeRobot: ModeRobot.MANUAL,
    notice: "",
    connection: Connection.CONNECTED,
    operationStatus: OperationStatus.PAUSE,
    levelBattery: 0,
    speed: 0,
    workstation: wsState.workstation
};
const plot : Plot = { text: "", from: 0, to: 0 };
const robotDataBand: RobotDataBand = {
    name: "",
    connection: {
        average: { connected: 0, desconnected: 0 },
        duration: { connected: 0, desconnected: 0 },
        frequency: { connected: 0, desconnected: 0 },
        interval: { connected: [plot], desconnected: [plot] }
    },
    mode: {
        average: { auto: 0, manual: 0 },
        duration: { auto: 0, manual: 0 },
        frequency: { auto: 0, manual: 0 },
        interval: { auto: [plot], manual: [plot] }
    },
    operationStatus: {
        average: { normal: 0, ems: 0 , pause: 0},
        duration:  { normal: 0, ems: 0 , pause: 0},
        frequency:  { normal: 0, ems: 0 , pause: 0},
        interval: { normal: [plot], ems: [plot] , pause: [plot]}
    },
    statusRobot: {
        average: { running: 0, inactive: 0 , waiting: 0},
        duration:  { running: 0, inactive: 0 , waiting: 0},
        frequency:  { running: 0, inactive: 0 , waiting: 0},
        interval: { running: [plot], inactive: [plot] , waiting: [plot]}
    },
    battery: {
        average: { charge: 0, standby: 0 , discharge: 0},
        duration:{ charge: 0, standby: 0 , discharge: 0},
        frequency:{ charge: 0, standby: 0 , discharge: 0},
        interval: { charge: [plot], standby: [plot] , discharge: [plot]}
    },
    speed: {
        average: { max: 0, standby: 0 , min: 0},
        duration: { max: 0, standby: 0 , min: 0},
        frequency: { max: 0, standby: 0 , min: 0},
        interval: { max: [plot], standby: [plot] , min: [plot]}
    }
}
const listRobots: RobotDto[] = [];
const listRobotsData: RobotDataChart[] = [];
const settingRobot: RobotSettingDto = { distance: { min: 4, max: 5 }, speed: { min: 4, max: 8 } };
const panelRobot: CountRobotsProperties = { count: 0, connected: 0,disconnected:0, running: 0,waiting:0,inactive:0,
    normal: 0,ems: 0,pause: 0, auto: 0,manual:0}
const typeConnection: readonly { label: string; value: Connection }[] = [
    { label: 'CONNECTED', value: Connection.CONNECTED },
    { label: 'DISCONNECTED', value: Connection.DISCONNECTED }];
const ListModeRobot: readonly { label: string; value: ModeRobot }[] = [
    { label: 'AUTO', value: ModeRobot.AUTO },
    { label: 'MANUAL', value: ModeRobot.MANUAL }];
const ListOperationStatus: readonly { label: string; value: OperationStatus }[] = [
    { label: 'EMS', value: OperationStatus.EMS },
    { label: 'PAUSE', value: OperationStatus.PAUSE }];
const ListStatusRobot: readonly { label: string; value: StatusRobot }[] = [
    { label: 'WAITING', value: StatusRobot.WAITING },
    { label: 'RUNNING', value: StatusRobot.RUNNING },
    { label: 'INACTIVE', value: StatusRobot.INACTIVE }];

export const robotState: RobotState = {
    typeConnection: typeConnection,
    listModeRobot: ListModeRobot,
    listOperationStatus: ListOperationStatus,
    listStatusRobot: ListStatusRobot,
    settingRobot: settingRobot,
    count: panelRobot,
    dataSource : new MatTableDataSource<RobotDto>(),
    robot: robot,
    robotDataChart : robotDataChart,
    listRobotsData:listRobotsData,
    listRobots: listRobots,
    robotDataBand: robotDataBand, 
    listRobotDataBand:[robotDataBand],
    errorMessage:""
};