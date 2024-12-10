import { Injectable } from '@angular/core';
import { RobotDto } from '../store/models/Robot/RobotDto.model';
import { Service } from './globalservice.service';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MailClientService  extends Service {

  constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) {  super(http, router, activeRoute);}

//   insert( msg :any  , files: []): Observable<HttpResponse<any>> {
// //const file = event.target.files[0];
// // const formData = new FormData();
//    const formData = new FormData();
//    formData.append("files", JSON.stringify(files));
//    formData.append("msg", JSON.stringify(msg));

//     return this.http.post(`${this.url}/mail`, formData,
//       { observe: 'response', headers: new HttpHeaders({
//         //'Authorization': "Bearer " + this.getAuthenticationRequest().token 
//     }) })
//   }
getAllAddressMail(): Observable<HttpResponse<any>> {
  return this.http.get(`${this.url}/mail-service/mail/all-address-mail`,
    { observe: 'response', headers: new HttpHeaders(
      //{ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }
    ) })
}
sendMail(files: File[], msg: any): Observable<HttpResponse<any>> {
  const formData: FormData = new FormData();
  formData.append('msg', new Blob([JSON.stringify(msg)], { type: 'application/json' }));

  if (files) {
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i], files[i].name);
    }
  }

  const headers = new HttpHeaders({
    'Accept': 'application/json'
  });

 // return this.http.post(`${this.url}/mail`, formData, { headers });
 return this.http.post(`${this.url}/mail-service/mail`, formData,
 { observe: 'response', headers })
}


}
