import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Service } from './globalservice.service';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class NotificationService extends Service {
  constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) {
    super(http, router, activeRoute);
  }
  getAll(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/notification-service/user`,
      {
        observe: 'response', headers: new HttpHeaders({
          'Content-Type': 'application/json', 'Authorization': "Bearer " + this.getAuthenticationRequest().token
        })
      })
  }
}
