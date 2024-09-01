import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { LevelType } from 'src/app/core/store/models/Notification/LevelType.enum';
import { Notification } from 'src/app/core/store/models/Notification/norifcation.models';
import { getListNotifications, getNotification } from 'src/app/core/store/selectors/Global.selector';
import { globalState, GlobalState } from 'src/app/core/store/states/Global.state';

@Component({
  selector: 'app-dropdown-notifications',
  templateUrl: './dropdown-notifications.component.html',
  styleUrls: ['./dropdown-notifications.component.css']
})
export class DropdownNotificationsComponent  implements OnInit , OnDestroy  {
  private getListNotificationSub !: Subscription | undefined;
  private getNotificationSub !: Subscription | undefined;
  public stateOver = true;
  public notifications!: Notification[];
  constructor(private storeGlobal: Store<GlobalState>){ }

  ngOnInit(): void {
    this.getListNotificationSub = this.storeGlobal.select(getListNotifications).subscribe(item => {
      globalState.listNotifications = item;
      this.notifications = item;
    });
    this.getNotificationSub = this.storeGlobal.select(getNotification).subscribe(item => {
     // this.notifications.unshift(item);
    });

  }
  ngOnDestroy(): void {
    if (this.getListNotificationSub) { this.getListNotificationSub.unsubscribe(); }
    if (this.getNotificationSub) { this.getNotificationSub.unsubscribe(); }
  }

  stopPropagation(event: Event): void {
    event.stopPropagation();
  }

 onClickOverNotifications():void{this.stateOver = !this.stateOver;}
 
}
