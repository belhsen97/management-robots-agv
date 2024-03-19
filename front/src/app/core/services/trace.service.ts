import { Injectable } from '@angular/core';
import { TraceDto } from '../store/models/Trace/TraceDto.model';
import { Service } from './globalservice.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TraceService extends Service {

  public  listTraces : TraceDto[]=[];
  public workstation !: TraceDto;

  constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { 
    super(http, router, activeRoute);
  }
  getAll(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/trace`,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json',
      //'Authorization': "Bearer " + this.getAuthenticationRequest().token 
    })
     })
  }
  delete(id: any): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.url}/trace/${id}`,
      { observe: 'response',
       //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) 
      })
  }
  deleteAll(): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.url}/trace`,
      { observe: 'response',
       //headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) 
      })
  }
}
