import { Injectable } from '@angular/core';
import {  TagDto } from '../store/models/Tag/TagDto.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Service } from './globalservice.service';
import { Observable } from 'rxjs';
import { WorkstationDto } from '../store/models/Workstation/WorkstationDto.model';
import { MatTableDataSource } from '@angular/material/table';

@Injectable({
  providedIn: 'root'
})
export class TagService extends Service {

    public  listTags : TagDto[]=[];
    public workstation !: WorkstationDto;
    public tag : TagDto ={id:'',code:'',description:'',workstation:this.workstation};
    dataSource : MatTableDataSource<TagDto> = new MatTableDataSource<TagDto>(this.listTags);

     constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { 
      super(http, router, activeRoute);
      this.randomDataTags();
    }
  getAll(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/tag`,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json',
      //'Authorization': "Bearer " + this.getAuthenticationRequest().token 
    })
     })
  }
  insert(t: TagDto): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/tag`, t,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json',
        //'Authorization': "Bearer " + this.getAuthenticationRequest().token 
    }) })
  }
  update(id: string, t: TagDto): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/tag/${id}`, t,
      { observe: 'response',
       headers: new HttpHeaders({'Content-Type': 'application/json',
       // 'Authorization': "Bearer " + this.getAuthenticationRequest().token 
      }) })
  }
  delete(id: any): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.url}/tag/${id}`,
      { observe: 'response',
       //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) 
      })
  }








  randomDataTags(): void {
    for (var i = 0; i < 100; i++) {
      const count = i + 1;
      this.listTags[i] = {
        id: count.toString(),
        code: this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString(),
        description : "tag description "+count ,
        workstation: this.workstation
      };
    }
  }
}
