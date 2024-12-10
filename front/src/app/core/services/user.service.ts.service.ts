import { Injectable } from '@angular/core';
import { AuthenticationRequest } from '../store/models/User/AuthenticationRequest.model';
import { UserDto } from '../store/models/User/UserDto.model';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { Service } from './globalservice.service';

@Injectable({
  providedIn: 'root'
})
export class UserService extends Service {

  constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { super(http, router, activeRoute); }

  getAll(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/user-service/user`,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }

  getById(id: any): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/user-service/user/${id}`,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }

  getByUsername(usename: string): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/user-service/user/username/${usename}`,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }
  getByAuthenticate(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/user-service/user/authenticate`,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }
  // getByListUsernames(usenamesArray: string[]) : Observable<HttpResponse<any>>{
  //               return this.http.get(`${this.url}/user/select-by-usernames/${usenamesArray.join(',')}`,
  //                                    {observe : 'response'})
  //             }

  update(id: string, user: UserDto): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user-service/user/${id}`, user,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }

  delete(id: any): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.url}/user-service/user/${id}`,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }

  updatePhotoProfile(username: string, file: File): Observable<HttpResponse<any>> {
    const formData = new FormData();   // Create form data
    formData.append("file", file, file.name); // Store form name as "file" with file data
    return this.http.put(`${this.url}/user-service/user/photo-to-user/${username}`, formData,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }




  register(authRequestDto: AuthenticationRequest): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/user-service/authentication/register`, authRequestDto,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json' }) })
  }

  login(authRequest: AuthenticationRequest): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/user-service/authentication/authenticate`, authRequest, {
      observe: 'response'
      , headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    })
  }

  logout(): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/user-service/authentication/logout`, {}, {
      observe: 'response'
      , headers: new HttpHeaders({
        'Authorization': "Bearer " + this.getAuthenticationRequest().token
        //,'refresh-token': this. getAuthenticationRequest().refresh_token
      })
    })
  }

  sendMailCodeForgotPassword(username: string, email: string): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user-service/user/mail-code-forgot-password/${username}/${email}`, {},
      { observe: 'response' })
  }
  updateForgotPassword(code: string, newpassword: string): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user-service/user/update-forgot-password/${code}/${newpassword}`, {},
      { observe: 'response' })
  }
  updatePassword(username: string, currentPassword: string, newPassword: string): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user-service/user/update-password/${username}/${currentPassword}/${newPassword}`, {},
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }

  updateRoleWithPermission(username: string, currentRole: string, newRole: string): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user-service/user-service/account/update-role-permission/${username}/${currentRole}/${newRole}`, {},
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }

  updateRole(username: string, role: string): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user-service/user/set-role/${username}/${role}`, {},
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }
  enableUser(username: string, enable: boolean): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user-service/user/set-enable/${username}/${enable}`, {},
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }


}