import { Injectable } from '@angular/core';
import { Connection } from '../store/models/Robot/Connection.enum';
import { ModeRobot } from '../store/models/Robot/ModeRobot.enum';
import { StatusRobot } from '../store/models/Robot/StatusRobot.enum';
import { OperationStatus } from '../store/models/Robot/OperationStatus.enum';
import { RobotDto } from '../store/models/Robot/RobotDto.model';
import { Service } from './globalservice.service';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { robotState } from '../store/states/Robot.state';
import { wsState } from '../store/states/Worstation.state';

@Injectable({
  providedIn: 'root'
})
export class RobotService extends Service {

  constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { 
    super(http, router, activeRoute); 
   // this.randomDataRobots();
   // this.refreshValuesPanelRobot();
  }




  getAll(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/robot`,
      { observe: 'response', 
      //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token })
     })
  }
  getbyNameAllData(name:String): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/robot/${name}/data/all`,
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
      robotState.listRobots[i] = {
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
        workstation: wsState.workstation
      };
    }
  }
  public refreshValuesPanelRobot(): void {
    robotState.panelRobot!.count = robotState.listRobots.length;
    robotState.panelRobot!.connected = robotState.listRobots.filter(robot => {return robot.connection == Connection.CONNECTED}).length;
    robotState.panelRobot!.auto = robotState.listRobots.filter(robot => {return robot.modeRobot == ModeRobot.AUTO }).length;
    robotState.panelRobot!.operationStatus = robotState.listRobots.filter(robot => {return robot.operationStatus == OperationStatus.EMS || robot.operationStatus == OperationStatus.PAUSE  }).length;
    robotState.panelRobot!.running  = robotState.listRobots.filter(robot => {return robot.statusRobot == StatusRobot.RUNNING }).length;
    }
}
