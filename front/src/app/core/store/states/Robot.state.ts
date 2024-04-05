import { Connection } from '../models/Robot/Connection.enum';
import { ModeRobot } from '../models/Robot/ModeRobot.enum';
import { StatusRobot } from '../models/Robot/StatusRobot.enum';
import { OperationStatus } from '../models/Robot/OperationStatus.enum';
import { RobotDto } from '../models/Robot/RobotDto.model';
import { SettingRobot } from '../models/Robot/SettingRobot.model';
import { PanelRobot } from '../models/Robot/PanelRobot.model';
import { wsState } from './Worstation.state';

export interface RobotState {
    typeConnection: ReadonlyArray<{ label: string; value: Connection }>;
    listModeRobot: ReadonlyArray<{ label: string; value: ModeRobot }>;
    listOperationStatus: ReadonlyArray<{ label: string; value: OperationStatus }>;
    listStatusRobot: ReadonlyArray<{ label: string; value: StatusRobot }>;
    settingRobot: SettingRobot;
    panelRobot: PanelRobot;
    robot: RobotDto;
    listRobots: RobotDto[];
}

export const robot: RobotDto = {
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
    //workstation: workstation,
    robot: robot,
    listRobots: listRobots
};