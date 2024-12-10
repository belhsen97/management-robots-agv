import { Injectable } from '@angular/core';
import { Connection } from '../store/models/Robot/Connection.enum';
import { ModeRobot } from '../store/models/Robot/ModeRobot.enum';
import { StatusRobot } from '../store/models/Robot/StatusRobot.enum';
import { OperationStatus } from '../store/models/Robot/OperationStatus.enum';
import { RobotDto } from '../store/models/Robot/RobotDto.model';
import { Service } from './globalservice.service';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { robotState } from '../store/states/Robot.state';
import { RobotSettingDto } from '../store/models/Robot/RobotSettingDto.model';

@Injectable({
  providedIn: 'root'
})
export class RobotService extends Service {

  constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) {   super(http, router, activeRoute);}

  getAll(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/trackbot-service/robot`,
      { observe: 'response', 
      //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token })
     })
  }
  getAllRobotConfig(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/trackbot-service/robot-setting`,
      { observe: 'response', 
      //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token })
     })
  }
  getById(id:string): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/trackbot-service/robot/${id}`,
      { observe: 'response', 
      //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token })
     })
  }
  getByName(name:string): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/trackbot-service/robot/name/${name}`,
      { observe: 'response', 
      //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token })
     })
  }






  geDataChartRobot(  queryParams: any): Observable<HttpResponse<any>> {
    let params = new HttpParams();
    if (queryParams) {
      Object.keys(queryParams).forEach(key => {
        const value = queryParams[key];
        if (value !== null && value !== undefined) { // Check for null or undefined
          params = params.append(key, value.toString());
        }
      });
    }
    return this.http.get(`${this.url}/trackbot-service/robot/data-chart`,
      { observe: 'response', params: params,
      //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token })
     })
  }








  geAllDataBand(queryParams: any): Observable<HttpResponse<any>> {
    let params = new HttpParams();
    if (queryParams) {
      Object.keys(queryParams).forEach(key => {
        const value = queryParams[key];
        if (value !== null && value !== undefined) { // Check for null or undefined
          params = params.append(key, value.toString());
        }
      });
    }
    return this.http.get(`${this.url}/trackbot-service/robot/databand`,
      { observe: 'response', params: params,
      //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token })
     })
  }
 insert(r: RobotDto): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/trackbot-service/robot`, r,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json',
      'Authorization': "Bearer " + this.getAuthenticationRequest().token 
    }) })
  }
  update(id: String, r: RobotDto): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/trackbot-service/robot/${id}`, r,
      { observe: 'response',
       headers: new HttpHeaders({'Content-Type': 'application/json',
       'Authorization': "Bearer " + this.getAuthenticationRequest().token 
      }) })
  }
  updateRobotConfig(r: RobotSettingDto): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/trackbot-service/robot-setting`, r,
      { observe: 'response',
       headers: new HttpHeaders({'Content-Type': 'application/json',
       'Authorization': "Bearer " + this.getAuthenticationRequest().token 
      }) })
  }
  delete(id: any): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.url}/trackbot-service/robot/${id}`,
      { observe: 'response',
       headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) 
      })
  }

  randomDataRobots(): void {
    for (var i = 0; i < 50; i++) {
      const count = i + 1;
      robotState.listRobots[i] = {
        id: count.toString(),
        clientid: 'robot-'+count.toString(),
        username: 'robot-'+count.toString(),
        password: 'robot-'+count.toString(),
        createdAt: new Date(),
        name: "Robot-" + count,
        statusRobot: this.getRandomEnumValue(StatusRobot),
        modeRobot: this.getRandomEnumValue(ModeRobot),
        notice: "vide",
        connection: this.getRandomEnumValue(Connection),
        operationStatus: this.getRandomEnumValue(OperationStatus),
        levelBattery: this.getRandomNumber(10, 100),
        speed: this.getRandomNumber(1.5, 20),
        distance : 0,
        codeTag : ""
      };
    }
  }
}
