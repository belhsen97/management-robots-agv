import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Store } from '@ngrx/store';
import { ShowAlert } from 'src/app/core/store/Global/App.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { UserService } from 'src/app/core/store/services/user.service.ts.service';

@Component({
  selector: 'app-form-forgot-password',
  templateUrl: './form-forgot-password.component.html',
  styleUrls: ['./form-forgot-password.component.css']
})
export class FormForgotPasswordComponent {
  constructor(public userService : UserService,  private store: Store){}


  onClickForgotPassword(form: NgForm):void {
    if (!form.invalid) { 
      this.userService.sendMailCodeForgotPassword(this.userService.authRequestDto.username, this.userService.authRequestDto.email ).subscribe(
        (response) => {
          this.userService.msgReponseStatus = response.body;
          this.store.dispatch( ShowAlert(  this.userService.msgReponseStatus) ); 
         }
      ,(error) => { 
        this.userService.msgReponseStatus =  { title : "Error", datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message};
        this.store.dispatch( ShowAlert(  this.userService.msgReponseStatus) ); 
      });
     }
  }
}
