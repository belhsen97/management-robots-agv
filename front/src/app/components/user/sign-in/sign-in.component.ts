import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserService } from 'src/app/core/services/user.service.ts.service';
import { GlobalState, globalState } from 'src/app/core/store/states/Global.state';
import { AuthenticationResponse } from 'src/app/core/store/models/User/AuthenticationResponse.model';
import { userState } from 'src/app/core/store/states/User.state';
import { UserDto } from 'src/app/core/store/models/User/UserDto.model';
import { loadAllNotifications } from 'src/app/core/store/actions/Global.Action';
import { Store } from '@ngrx/store';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {
  constructor(public userService : UserService,   private storeGlobal: Store<GlobalState> ){}


  signIn(form: NgForm):void {
    if(!form.invalid){
      this.userService.login(this.userService.authRequest).subscribe(
        (response) => { 
          this.userService.authResponse = response.body; 
          
          globalState.msgResponseStatus = {
            title:this.userService.authResponse.title,
            datestamp:this.userService.authResponse.datestamp,
            status:this.userService.authResponse.status,
            message:this.userService.authResponse.message};
            if ( globalState.msgResponseStatus .status ===  globalState.ReponseStatus.successful ) 
            {
              this.saveLogin( this.userService.authResponse,this.userService.authRequest.username );
            }
        }
        );
     }
  }



  saveLogin(authResponseDto: AuthenticationResponse, username: string): void {
    this.userService.setToken(authResponseDto);
    this.userService.getByUsername(username).subscribe(
      (response) => {
        userState.userDto = response.body as UserDto;
        //this.userService.setUserDto(userState.userDto);
        //this.userService.goToComponent('user/profile/'+this.userDto.username); // ??????????????????
        this.storeGlobal.dispatch(loadAllNotifications());
        this.userService.goToComponent('/dashboard/table'); // ??????????????????
      }
    );
  }







   
}