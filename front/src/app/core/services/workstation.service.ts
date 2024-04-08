import { Injectable } from '@angular/core';
import { WorkstationDto } from '../store/models/Workstation/WorkstationDto.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Service } from './globalservice.service';
import { Observable } from 'rxjs';
import {  wsState } from '../store/states/Worstation.state';

@Injectable({
  providedIn: 'root'
})
export class WorkstationService extends Service {

   constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { 
    super(http, router, activeRoute);//this.randomDataWorkstations();
  }


  getAll(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/workstation`,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json',
      //'Authorization': "Bearer " + this.getAuthenticationRequest().token 
    })
     })
  }
  getById(id: any): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/workstation/${id}`,
      { observe: 'response', headers: new HttpHeaders({  'Content-Type': 'application/json',
        //'Authorization': "Bearer " + this.getAuthenticationRequest().token 
      }) })
  }
  insert(Wd: WorkstationDto): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/workstation`, Wd,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json',
        //'Authorization': "Bearer " + this.getAuthenticationRequest().token 
    }) })
  }
  update(id: string, Wd: WorkstationDto): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/workstation/${id}`, Wd,
      { observe: 'response',
       headers: new HttpHeaders({'Content-Type': 'application/json',
       // 'Authorization': "Bearer " + this.getAuthenticationRequest().token 
      }) })
  }
  delete(id: any): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.url}/workstation/${id}`,
      { observe: 'response',
       //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) 
      })
  }
  randomDataWorkstations(): void {
    for (var i = 0; i < 2; i++) {
      const count = i + 1;
      wsState.listWorkstations[i] = {
        id: count.toString(),
        name : "Workstation-"+count,
        enable : true,
        tags : [],
        robots: []
      };
    }
  }
}
