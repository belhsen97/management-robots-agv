import { Connection } from '../models/Robot/Connection.enum';
import { ModeRobot } from '../models/Robot/ModeRobot.enum';
import { StatusRobot } from '../models/Robot/StatusRobot.enum';
import { OperationStatus } from '../models/Robot/OperationStatus.enum';
import { RobotDto } from '../models/Robot/RobotDto.model';
import { SettingRobot } from '../models/Robot/SettingRobot.model';
import { PanelRobot } from '../models/Robot/PanelRobot.model';
import { wsState } from './Worstation.state';
import { MatTableDataSource } from '@angular/material/table';
import { RobotData } from '../models/Robot/RobotData.model';

export interface RobotState {
    typeConnection?: ReadonlyArray<{ label: string; value: Connection }>;
    listModeRobot?: ReadonlyArray<{ label: string; value: ModeRobot }>;
    listOperationStatus?: ReadonlyArray<{ label: string; value: OperationStatus }>;
    listStatusRobot?: ReadonlyArray<{ label: string; value: StatusRobot }>;
    settingRobot?: SettingRobot;
    panelRobot?: PanelRobot;
    dataSource?: MatTableDataSource<RobotDto>;
    robotData?:RobotData;
    robot: RobotDto;
    listRobotsData?:RobotData[];
    listRobots: RobotDto[];
}

const robotData: RobotData = {
    name: "robot-1",
    speed: [[1647437400000, 8.59],[1647523800000, 7.5]],
    battery: [[1647437400000, 90.5],[1647523800000, 80.0]],
    statusRobot:[[1647437400000, 0],[1647523800000, 1]],
    operationStatus:[[1647437400000, 0],[1647523800000, 1]]
};
const robot: RobotDto = {
    id: "",
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
const listRobots: RobotDto[] = [];
const listRobotsData: RobotData[] = [];
const settingRobot: SettingRobot = { distance: { min: 4, max: 5 }, speed: { min: 4, max: 8 } };
const panelRobot: PanelRobot = { count: 20, connected: 10, running: 20, operationStatus: 10, auto: 10 }
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
    panelRobot: panelRobot,
    dataSource : new MatTableDataSource<RobotDto>(),
    robot: robot,
    robotData : robotData,
    listRobotsData:listRobotsData,
    listRobots: listRobots
};