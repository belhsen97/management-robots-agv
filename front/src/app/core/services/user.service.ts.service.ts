import { Injectable } from '@angular/core';
import { Role } from '../store/models/User/Role.enum';
import { Gender } from '../store/models/User/Gender.enum';
import { AuthenticationRequestDto } from '../store/models/User/AuthenticationRequestDto.model';
import { ReponseStatus } from '../store/models/Global/ReponseStatus.enum';
import { AuthenticationResponseDto } from '../store/models/User/AuthenticationResponseDto.model';
import { UserDto } from '../store/models/User/UserDto.model';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { Service } from './globalservice.service';

@Injectable({
  providedIn: 'root'
})
export class UserService extends Service {


  public readonly FEMALE: Gender = Gender.FEMALE;
  public readonly MALE: Gender = Gender.MALE;
  public userDto: UserDto = {
    no: 1,
    id: "1",
    createdAt: new Date(),
    username: "username",
    password: "password",
    code: "code",
    role: Role.OPERATOR,
    enabled: true,
    firstname: "firstname",
    lastname: "lastname",
    matricule: "matricule",
    phone: 12345678,
    email: "email@exemple.com",
    gender: Gender.MALE,
    photo: {
      id: "1",
      fileName: "user",
      downloadURL: "assets/images/user/user.png",
      fileType: "png",
      fileSize: 120,
    }
  };




  public ListUsers: UserDto[] = [this.userDto, this.userDto, this.userDto, this.userDto, this.userDto, this.userDto];
  private readonly listPathPermision = [
    { role: Role.ADMIN, path: '/edit-user/**' },
    { role: Role.MAINTENANCE, path: '/edit-user/**' },
    { role: Role.OPERATOR, path: '/edit-user/**' },

    { role: Role.MAINTENANCE, path: '/list-workstations/**' },
    { role: Role.MAINTENANCE, path: '/list-workstations/**' }];
 

  public readonly listRole = [{ label: 'ADMIN', value: Role.ADMIN }, { label: 'MAINTENANCE', value: Role.MAINTENANCE }, { label: 'OPERATOR', value: Role.OPERATOR }];
  public readonly listGender = [{ label: 'FEMALE', value: Gender.FEMALE }, { label: 'MALE', value: Gender.MALE }];
  public photoUser: string = "assets/images/user/user-2.png";


  public readonly SUCCESSFUL: ReponseStatus = ReponseStatus.SUCCESSFUL;
  public readonly ERROR: ReponseStatus = ReponseStatus.ERROR;
  public readonly UNSUCCESSFUL: ReponseStatus = ReponseStatus.UNSUCCESSFUL;






  constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { super(http, router, activeRoute); }







  getAll(): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/user`,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }

  getById(id: any): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/user/${id}`,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }
  getByUsername(usename: string): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/user/username/${usename}`,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }
  // getByListUsernames(usenamesArray: string[]) : Observable<HttpResponse<any>>{
  //               return this.http.get(`${this.url}/user-service/account/select-by-usernames/${usenamesArray.join(',')}`,
  //                                    {observe : 'response'})
  //             }
  update(id: string, user: UserDto): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user/${id}`, user,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }
  delete(id: any): Observable<HttpResponse<any>> {
    return this.http.delete(`${this.url}/user/${id}`,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }


  updatePhotoProfile(username: string, file: File): Observable<HttpResponse<any>> {
    const formData = new FormData();   // Create form data
    formData.append("file", file, file.name); // Store form name as "file" with file data
    return this.http.put(`${this.url}/user/photo-to-user/${username}`, formData,
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }








  register(authRequestDto: AuthenticationRequestDto): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/user/register`, authRequestDto,
      { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json' }) })
  }

  login(authRequest: AuthenticationRequestDto): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/authentication/authenticate`, authRequest, {
      observe: 'response'
      , headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    })
  }
  saveLogin(authResponseDto: AuthenticationResponseDto, username: string): void {
    this.setToken(authResponseDto);
    this.getByUsername(username).subscribe(
      (response) => {
        this.userDto = response.body;
        this.setUserDto(this.userDto);
        console.log(this.userDto);
        //this.goToComponent('user/profile/'+this.userDto.username); // ??????????????????
        this.goToComponent('/dashboard/table'); // ??????????????????
      }
      , (error) => { console.log("AuthenticationService saveLogin " + error.message) });
  }

  logout(): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}/authentication/logout`, {}, {
      observe: 'response'
      , headers: new HttpHeaders({
        'Authorization': "Bearer " + this.getAuthenticationRequest().token
        //,'refresh-token': this. getAuthenticationRequest().refresh_token
      })
    })
  }

  sendMailCodeForgotPassword(username: string, email: string): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user/mail-code-forgot-password/${username}/${email}`, {},
      { observe: 'response' })
  }
  updateForgotPassword(code: string, newpassword: string): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user/update-forgot-password/${code}/${newpassword}`, {},
      { observe: 'response' })
  }
  updatePassword(username: string, currentPassword: string, newPassword: string): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user/update-password/${username}/${currentPassword}/${newPassword}`, {},
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }

  updateRoleWithPermission(username: string, currentRole: string, newRole: string): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user-service/account/update-role-permission/${username}/${currentRole}/${newRole}`, {},
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }

  updateRole(username: string, role: string): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user/set-role/${username}/${role}`, {},
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }
  enableUser(username: string, enable: boolean): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/user/set-enable/${username}/${enable}`, {},
      { observe: 'response', headers: new HttpHeaders({ 'Authorization': "Bearer " + this.getAuthenticationRequest().token }) })
  }




  isAuthenticated(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    console.log('state.url: ', state.url);
    const pathFromRoot = route.pathFromRoot.map(route => route.url.join('/')).join('/');
    console.log('Full path: ', pathFromRoot);
    console.log(this.isPathValid(pathFromRoot, this.listPathPermision));
    return true;//????????????????????????????????????????????????????????????????????????????????????
  }
  private isPathValid(pathToCheck: string,listPathPermission:any): boolean {
    for (const pathPermission of listPathPermission) {
      const pathPattern = pathPermission.path.replace(/\*\*/g, '.*');
      const regex = new RegExp(`^${pathPattern}$`); 

      if (regex.test(pathToCheck)&&this.getUserDto().role == pathPermission.role ) { 
        return true;
      }
    }
    return false;
  }

}
