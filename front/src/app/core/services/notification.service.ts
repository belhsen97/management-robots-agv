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
export class NotificationService extends Service {

  constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { 
   super(http, router, activeRoute);
 }
 getAll(): Observable<HttpResponse<any>> {
  return this.http.get(`${this.url}/notification`,
    { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json',
    //'Authorization': "Bearer " + this.getAuthenticationRequest().token 
  })
   })
}

}
