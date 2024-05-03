import { WorkstationDto } from "../Workstation/WorkstationDto.model";
import { Connection } from "./Connection.enum";
import { ModeRobot } from "./ModeRobot.enum";
import { OperationStatus } from "./OperationStatus.enum";
import { StatusRobot } from "./StatusRobot.enum";

export interface RobotDto {
    id: String;
    createdAt: Date;
    clientid: String;
    username: String;
    password: String;
    name: string;
       statusRobot: StatusRobot;
       modeRobot: ModeRobot;
    notice: string;
       connection: Connection;
       operationStatus: OperationStatus;
       levelBattery: number;  // real time
       speed: number; // real time
    workstation: WorkstationDto;
}