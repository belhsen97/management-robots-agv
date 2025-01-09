import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { ShowAlert, openSidebar, searchInput } from 'src/app/core/store/actions/Global.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { UserService } from 'src/app/core/services/user.service.ts.service';
import { GlobalState } from 'src/app/core/store/states/Global.state';
import { UserState, userState } from 'src/app/core/store/states/User.state';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  userState !: UserState;
  constructor(private storeGlobal: Store<GlobalState>, public userService: UserService) { this.userState = userState; }
  state: boolean = false;
  ngOnInit(): void { this.verifyCordinateUserAthenticate(); }
  onKeyupSearchInput($event: any): void {
    this.storeGlobal.dispatch(searchInput({ value: $event.target.value }));
  }
  onClickMenuSideBar(): void {
    this.state = !this.state;
    this.storeGlobal.dispatch(openSidebar({ IsOpen: this.state }));
  }
  verifyCordinateUserAthenticate(): void {
    if (this.userState.userDto!.firstname == null || this.userState.userDto!.lastname == null) {
      this.storeGlobal.dispatch(ShowAlert({ title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: "You must add your first and last name!" }));
      this.userService.goToComponent('/user/edit');
    }
  }
  onClickSignOut(): void {
    this.userService.logout().subscribe(
      (response) => {
        this.userService.clearAuthenticationRequest();
        this.userState.userDto = null;
        userState.userDto = null;
        this.userService.goToComponent('sign-in');
      }
      , (error) => {
        this.userService.msgResponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
        this.storeGlobal.dispatch(ShowAlert(this.userService.msgResponseStatus));
      });
  }
}
