import { Injectable } from '@angular/core';
import { WorkstationDto } from '../store/models/Workstation/WorkstationDto.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Service } from './globalservice.service';
import { Tag } from '../store/models/Workstation/Tag.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WorkstationService extends Service {

  public readonly  listTags : Tag[]=[];
  public listWorkstations  : WorkstationDto[] =[];
  
  public tag : Tag ={
    id:1,
    code: "000747643911405335" ,
    description : "description"
   };

  public workstation : WorkstationDto ={
    no: 1,
    id:"1",
    name: "workstation 1" ,
    enable: true,
    tags:[this.listTags[0],this.listTags[3]],
    robots: []
   };


   constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { 
    super(http, router, activeRoute);
    this.randomDataTags();
    //this.randomDataWorkstations();
  }


  getAll(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/workstation`,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json',
      //'Authorization': "Bearer " + this.getAuthenticationRequest().token 
    })
     })
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





  randomDataTags(): void {
    for (var i = 0; i < 100; i++) {
      const count = i + 1;
      this.listTags[i] = {
        id: count,
        code: this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString(),
        description : "tag description "+count 
      };
    }
  }
  randomDataWorkstations(): void {
    for (var i = 0; i < 2; i++) {
      const count = i + 1;
      this.listWorkstations[i] = {
        no: count,
        id: count.toString(),
        name : "Workstation-"+count,
        enable : true,
        tags : [],
        robots: []
      };
    }
  }


}
