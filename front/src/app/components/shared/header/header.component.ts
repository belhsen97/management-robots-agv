import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { ShowAlert, openSidebar, searchInput } from 'src/app/core/store/actions/Global.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { UserService } from 'src/app/core/services/user.service.ts.service';
import { GlobalState, globalState } from 'src/app/core/store/states/Global.state';
import { UserState, userState } from 'src/app/core/store/states/User.state';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  userState !: UserState;
  constructor(private store: Store<GlobalState> , public userService : UserService ){ this.userState = userState;   }
  state : boolean = false;

  onKeyupSearchInput($event:any):void{
   // console.log($event.target.value ); 
     this.store.dispatch(searchInput({ value:  $event.target.value}));
   }

  
   onClickMenuSideBar():void{
      this.state  = !this.state; 
      this.store.dispatch(openSidebar({ IsOpen:   this.state }));
   }

   onClickSignOut():void{
    this.userService.logout().subscribe(
      (response) => {
        //userState.userDto = defaultUserDto;
        this.userService.clearAll();  
        this.userService.goToComponent('sign-in');}
      ,(error) => {
        this.userService.msgResponseStatus  =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        this.store.dispatch( ShowAlert( this.userService.msgResponseStatus ) ); 
      }) ;
   }
}
