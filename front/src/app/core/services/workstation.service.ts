import { Injectable } from '@angular/core';
import { WorkstationDto } from '../store/models/Workstation/WorkstationDto.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Service } from './globalservice.service';
import { TagDto } from '../store/models/Tag/TagDto.model';
import { Observable } from 'rxjs';
import { MatTableDataSource } from '@angular/material/table';

@Injectable({
  providedIn: 'root'
})
export class WorkstationService extends Service {

  public  listTags : TagDto[]=[];
  public listWorkstations  : WorkstationDto[] =[];
  dataSource : MatTableDataSource<WorkstationDto> = new MatTableDataSource<WorkstationDto>(this.listWorkstations);

  
  public workstation : WorkstationDto ={
    id:"1",
    name: "workstation 1" ,
    enable: true,
    tags:[],
    robots: []
   };

  public tag : TagDto ={
    id:"1",
    code: "000747643911405335" ,
    description : "description",
    workstation : this.workstation
   };



   constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { 
    super(http, router, activeRoute);
    
    //this.randomDataWorkstations();
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
      this.listWorkstations[i] = {
        id: count.toString(),
        name : "Workstation-"+count,
        enable : true,
        tags : [],
        robots: []
      };
    }
  }


}
