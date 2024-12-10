import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { LevelType } from 'src/app/core/store/models/Notification/LevelType.enum';
import { Badge, Notification } from 'src/app/core/store/models/Notification/norifcation.models';
import { getListNotifications, getNotification } from 'src/app/core/store/selectors/Global.selector';
import { globalState, GlobalState } from 'src/app/core/store/states/Global.state';

@Component({
  selector: 'app-dropdown-notifications',
  templateUrl: './dropdown-notifications.component.html',
  styleUrls: ['./dropdown-notifications.component.css']
})
export class DropdownNotificationsComponent  implements OnInit , OnDestroy  {
  private getListNotificationSub !: Subscription ;
  private getNotificationSub !: Subscription;
  public stateOver = true;
  public notifications!: Notification[];
  public badge: Badge={color:"#d7dde1",count:0};
  constructor(private storeGlobal: Store<GlobalState>){ }

  ngOnInit(): void {
    this.getListNotificationSub = this.storeGlobal.select(getListNotifications).subscribe(item => {
      //globalState.listNotifications = item;
      this.notifications = item; 
      this.badge.color = "#FF0000";   this.badge.count = item.length;
      console.log("A1");
    });
    this.getNotificationSub = this.storeGlobal.select(getNotification).subscribe(item => { 
      if (item && !this.notifications.some(notification =>
        notification.senderName === item.senderName &&
        notification.createdAt === item.createdAt &&
        notification.level === item.level &&
        notification.message === item.message
      )) {
        this.notifications.unshift(item);
      }
    });
  }
  ngOnDestroy(): void {
    this.getListNotificationSub.unsubscribe(); 
    this.getNotificationSub.unsubscribe(); 
  }

  stopPropagation(event: Event): void {
    event.stopPropagation();
  }

 onClickOverNotifications():void{this.stateOver = !this.stateOver;}
 
}
