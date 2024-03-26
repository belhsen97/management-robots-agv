import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms'; 
import { Store } from '@ngrx/store';
import { ShowAlert } from 'src/app/core/store/Global/App.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { UserService } from 'src/app/core/services/user.service.ts.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent{

constructor(public userService : UserService,  private store: Store){}


signUp(form: NgForm):void {
  if(!form.invalid){
    this.userService.register(this.userService.authRequest).subscribe(
      (response) => {     
        this.userService.msgReponseStatus = response.body;
        if ( this.userService.msgReponseStatus.status === this.userService.SUCCESSFUL ) 
        { this.userService.goToComponent("success-sign-up/"+this.userService.authRequest.email);}
        console.log(this.userService.msgReponseStatus);
      }
      ,
      (error:HttpErrorResponse) => {
        if (error.status === 406 ||error.status === 404|| error.status === 500 ) {   this.userService.msgReponseStatus = error.error; }
        else {
          this.userService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        }
        this.store.dispatch( ShowAlert(  this.userService.msgReponseStatus) );  console.log(error.status) ;
      }  
      );
 }}



}
