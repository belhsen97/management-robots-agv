import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Store } from '@ngrx/store';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { UserService } from 'src/app/core/services/user.service.ts.service';
import { globalState } from 'src/app/core/store/states/Global.state';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {
  constructor(public userService : UserService  , private store: Store){}


  signIn(form: NgForm):void {
    if(!form.invalid){
      this.userService.login(this.userService.authRequest).subscribe(
        (response) => { 
          this.userService.authResponse = response.body; 
          
          this.userService.msgResponseStatus   = {
            title:this.userService.authResponse.title,
            datestamp:this.userService.authResponse.datestamp,
            status:this.userService.authResponse.status,
            message:this.userService.authResponse.message}; 

            if ( this.userService.msgResponseStatus .status ===  globalState.ReponseStatus.successful ) 
            {
              this.userService.saveLogin( this.userService.authResponse,this.userService.authRequest.username );
            }
        }
        ,
        (error:HttpErrorResponse) => {
          if (error.status === 403 ) { this.userService.msgResponseStatus  = error.error; }
          else {
            this.userService.msgResponseStatus  =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          }
          this.store.dispatch( ShowAlert(globalState.msgResponseStatus ) );  console.log(error.status) ;
        }  
        );
     }
  }

   
}

