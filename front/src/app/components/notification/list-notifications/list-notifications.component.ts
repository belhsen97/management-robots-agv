import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { Notification } from 'src/app/core/store/models/Notification/norifcation.models';
import { getListNotifications, getNotification } from 'src/app/core/store/selectors/Global.selector';
import { globalState, GlobalState } from 'src/app/core/store/states/Global.state';


@Component({
  selector: 'app-list-notifications',
  templateUrl: './list-notifications.component.html',
  styleUrls: ['./list-notifications.component.css']
})
export class ListNotificationsComponent  implements OnInit , OnDestroy  {
  private getListNotificationSub !: Subscription;
  private getNotificationSub !: Subscription ;
  public stateOver = true;
  private notification!: Notification;
  public notifications!: Notification[];
  constructor(private storeGlobal: Store<GlobalState>){ }

  ngOnInit(): void {
    this.getListNotificationSub = this.storeGlobal.select(getListNotifications).subscribe(item => {
      this.notifications = item;
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
