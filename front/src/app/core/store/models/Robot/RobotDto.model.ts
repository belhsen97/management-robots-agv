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
    name: String;
    notice: String;


    statusRobot: StatusRobot;// real time
    modeRobot: ModeRobot;// real time
    connection: Connection;// real time
    operationStatus: OperationStatus;
    levelBattery: number;  // real time
    speed: number; // real time
    distance: number; // real time
    codeTag: String; // real time
}