import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Store } from '@ngrx/store';
import { ShowAlert } from 'src/app/core/store/Global/App.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { UserService } from 'src/app/core/store/services/user.service.ts.service';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {
  constructor(public userService : UserService  , private store: Store){}


  signIn(form: NgForm):void {
    if(!form.invalid){
      this.userService.login(this.userService.authRequestDto).subscribe(
        (response) => { 
          this.userService.authResponseDto = response.body; 
          this.userService.msgReponseStatus = {title:this.userService.authResponseDto.title,datestamp:this.userService.authResponseDto.datestamp,status:this.userService.authResponseDto.status,message:this.userService.authResponseDto.message}; 
            if ( this.userService.msgReponseStatus.status === this.userService.SUCCESSFUL ) 
            {
              this.userService.saveLogin( this.userService.authResponseDto,this.userService.authRequestDto.username );
            }
        }
        ,
        (error:HttpErrorResponse) => {
          if (error.status === 403 ) {   this.userService.msgReponseStatus = error.error; }
          else {
            this.userService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          }
          this.store.dispatch( ShowAlert(  this.userService.msgReponseStatus) );  console.log(error.status) ;
        }  
        );
     }
  }

   
}

