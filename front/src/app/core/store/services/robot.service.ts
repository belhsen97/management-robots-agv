import { Injectable } from '@angular/core';
import { Connection } from '../models/Robot/Connection.enum';
import { ModeRobot } from '../models/Robot/ModeRobot.enum';
import { StatusRobot } from '../models/Robot/StatusRobot.enum';
import { OperationStatus } from '../models/Robot/OperationStatus.enum';
import { RobotDto } from '../models/Robot/RobotDto.model';
import { SettingRobot } from '../models/Robot/SettingRobot.model';
import { WorkstationDto } from '../models/Workstation/WorkstationDto.model';
import { PanelRobot } from '../models/Robot/PanelRobot.model';
import { Service } from './globalservice.service';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class RobotService extends Service {

  public readonly typeConnection = [{ label: 'CONNECTED', value: Connection.CONNECTED },
  { label: 'DISCONNECTED', value: Connection.DISCONNECTED }];
  public readonly ListModeRobot = [{ label: 'AUTO', value: ModeRobot.AUTO },
  { label: 'MANUAL', value: ModeRobot.MANUAL }];
  public readonly ListOperationStatus = [{ label: 'EMS', value: OperationStatus.EMS },
  { label: 'PAUSE', value: OperationStatus.PAUSE }];
  public readonly ListStatusRobot = [{ label: 'WAITING', value: StatusRobot.WAITING },
  { label: 'RUNNING', value: StatusRobot.RUNNING }, { label: 'INACTIVE', value: StatusRobot.INACTIVE }];
  public settingRobot: SettingRobot = { distance: { min: 4, max: 5 }, speed: { min: 4, max: 8 } };


  public panelRobot: PanelRobot = { count: 20, connected: 10, running: 20, operationStatus: 10, auto: 10 }


  public workstation: WorkstationDto = {
    id: 1,
    name: "workstation 1",
    enable: true,
    tags: [],
    robots: []
  };
  public robot: RobotDto = {
    id: 1,
    createdAt: new Date(),
    name: "Robot",
    statusRobot: StatusRobot.RUNNING,
    modeRobot: ModeRobot.MANUAL,
    notice: "vide",
    connection: Connection.CONNECTED,
    operationStatus: OperationStatus.PAUSE,
    levelBattery: 98,
    speed: 1.5,
    workstation: this.workstation
  };


  public listRobots: RobotDto[] = [];




  constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { 
    super(http, router, activeRoute); 
    this.randomDataRobots();
    this.refreshValuesPanelRobot();
  }

  randomDataRobots(): void {
    for (var i = 0; i < 50; i++) {
      const count = i + 1;
      this.listRobots[i] = {
        id: count,
        createdAt: new Date(),
        name: "Robot-" + count,
        statusRobot: this.getRandomEnumValue(StatusRobot),
        modeRobot: this.getRandomEnumValue(ModeRobot),
        notice: "vide",
        connection: this.getRandomEnumValue(Connection),
        operationStatus: this.getRandomEnumValue(OperationStatus),
        levelBattery: this.getRandomNumber(10, 100),
        speed: this.getRandomNumber(1.5, 20),
        workstation: this.workstation
      };
    }
  }

   




 



  public refreshValuesPanelRobot(): void {
     this.panelRobot.count = this.listRobots.length;
     this.panelRobot.connected = this.listRobots.filter(robot => {return robot.connection == Connection.DISCONNECTED}).length;
     this.panelRobot.auto = this.listRobots.filter(robot => {return robot.modeRobot == ModeRobot.AUTO }).length;
     this.panelRobot.operationStatus = this.listRobots.filter(robot => {return robot.operationStatus == OperationStatus.EMS || robot.operationStatus == OperationStatus.PAUSE  }).length;
     this.panelRobot.running  = this.listRobots.filter(robot => {return robot.statusRobot == StatusRobot.RUNNING }).length;
    }
}
