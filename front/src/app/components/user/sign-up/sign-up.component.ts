import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms'; 
import { UserService } from 'src/app/core/services/user.service.ts.service';
import { globalState } from 'src/app/core/store/states/Global.state';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent{

constructor(public userService : UserService){}

signUp(form: NgForm):void {
  if(!form.invalid){
    this.userService.register(this.userService.authRequest).subscribe(
      (response) => {     
        this.userService.msgResponseStatus  = response.body;
        if (this.userService.msgResponseStatus .status === globalState.ReponseStatus.successful ) 
        { this.userService.goToComponent("success-sign-up/"+this.userService.authRequest.email);}
        console.log(this.userService.msgResponseStatus );
      }
      );
 }}

}
