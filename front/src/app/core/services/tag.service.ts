import { Injectable } from '@angular/core';
import {  TagDto } from '../store/models/Tag/TagDto.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Service } from './globalservice.service';
import { Observable } from 'rxjs';
import { tagState } from '../store/states/Tag.state';
import { wsState } from '../store/states/Worstation.state';

@Injectable({
  providedIn: 'root'
})
export class TagService extends Service {



     constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { 
      super(http, router, activeRoute);
      this.randomDataTags();
    }
  getAll(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/trackbot-service/tag`,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json',
      'Authorization': "Bearer " + this.getAuthenticationRequest().token 
    })
     })
  }
  insert(t: TagDto): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/trackbot-service/tag`, t,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json',
        'Authorization': "Bearer " + this.getAuthenticationRequest().token 
    }) })
  }
  update(id: string, t: TagDto): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/trackbot-service/tag/${id}`, t,
      { observe: 'response',
       headers: new HttpHeaders({'Content-Type': 'application/json',
       'Authorization': "Bearer " + this.getAuthenticationRequest().token 
      }) })
  }
  delete(id: any): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.url}/trackbot-service/tag/${id}`,
      { observe: 'response',
       headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) 
      })
  }








  randomDataTags(): void {
    for (var i = 0; i < 100; i++) {
      const count = i + 1;
      tagState.listTags[i] = {
        id: count.toString(),
        code: this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString(),
        workstation: wsState.workstation
      };
    }
  }
}
