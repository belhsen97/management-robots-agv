import { Injectable } from '@angular/core';
import { Connection } from '../store/models/Robot/Connection.enum';
import { ModeRobot } from '../store/models/Robot/ModeRobot.enum';
import { StatusRobot } from '../store/models/Robot/StatusRobot.enum';
import { OperationStatus } from '../store/models/Robot/OperationStatus.enum';
import { RobotDto } from '../store/models/Robot/RobotDto.model';
import { SettingRobot } from '../store/models/Robot/SettingRobot.model';
import { WorkstationDto } from '../store/models/Workstation/WorkstationDto.model';
import { PanelRobot } from '../store/models/Robot/PanelRobot.model';
import { Service } from './globalservice.service';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';

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
  public workstation: WorkstationDto = {id: "",name: "",enable: true,tags:[],robots: []};
  public robot: RobotDto = {
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
    workstation: this.workstation
  };
  public listRobots: RobotDto[] = [];




  constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { 
    super(http, router, activeRoute); 
    this.randomDataRobots();
    this.refreshValuesPanelRobot();
  }




  getAll(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/robot`,
      { observe: 'response', 
      //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token })
     })
  }
 insert(r: RobotDto): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/robot`, r,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json',
        //'Authorization': "Bearer " + this.getAuthenticationRequest().token 
    }) })
  }
  update(id: string, r: RobotDto): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/robot/${id}`, r,
      { observe: 'response',
       headers: new HttpHeaders({'Content-Type': 'application/json',
       // 'Authorization': "Bearer " + this.getAuthenticationRequest().token 
      }) })
  }
  delete(id: any): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.url}/robot/${id}`,
      { observe: 'response',
       //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) 
      })
  }










  randomDataRobots(): void {
    for (var i = 0; i < 50; i++) {
      const count = i + 1;
      this.listRobots[i] = {
        id: count.toString(),
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
