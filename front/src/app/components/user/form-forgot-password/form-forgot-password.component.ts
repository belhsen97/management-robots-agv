import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Store } from '@ngrx/store';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { UserService } from 'src/app/core/services/user.service.ts.service';
import { GlobalState } from 'src/app/core/store/states/Global.state';

@Component({
  selector: 'app-form-forgot-password',
  templateUrl: './form-forgot-password.component.html',
  styleUrls: ['./form-forgot-password.component.css']
})
export class FormForgotPasswordComponent {
  constructor(public userService : UserService,  private storeGlobal: Store<GlobalState>){}


  onClickForgotPassword(form: NgForm):void {
    if (!form.invalid) { 
      this.userService.sendMailCodeForgotPassword(this.userService.authRequest.username, this.userService.authRequest.email ).subscribe(
        (response) => {
          this.userService.msgResponseStatus  = response.body;
          this.storeGlobal.dispatch( ShowAlert(this.userService.msgResponseStatus ) ); 
         });
     }
  }
}
