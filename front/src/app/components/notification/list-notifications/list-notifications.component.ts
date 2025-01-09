import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { LevelType } from 'src/app/core/store/models/Notification/LevelType.enum';
import { Notification } from 'src/app/core/store/models/Notification/norifcation.models';
import { getListNotifications, getNotification } from 'src/app/core/store/selectors/Global.selector';
import { GlobalState } from 'src/app/core/store/states/Global.state';


@Component({
  selector: 'app-list-notifications',
  templateUrl: './list-notifications.component.html',
  styleUrls: ['./list-notifications.component.css']
})
export class ListNotificationsComponent implements OnInit, OnDestroy {
  private getListNotificationSub !: Subscription;
  private getNotificationSub !: Subscription;
  public stateOver = true;

  public list!: Notification[];
  public listFilter!: Notification[];
  public checkboxValues: Record<LevelType, boolean> = {
    [LevelType.INFO]: false,
    [LevelType.WARNING]: false,
    [LevelType.ERROR]: false,
    [LevelType.DEBUG]: false,
    [LevelType.TRACE]: false
  };


  constructor(private storeGlobal: Store<GlobalState>) { }

  ngOnInit(): void {
    this.getListNotificationSub = this.storeGlobal.select(getListNotifications).subscribe(item => {
      this.listFilter = item; this.list = item;
    });
    this.getNotificationSub = this.storeGlobal.select(getNotification).subscribe(item => {
      if (item && !this.list.some(notification =>
        notification.senderName === item.senderName &&
        notification.createdAt === item.createdAt &&
        notification.level === item.level &&
        notification.message === item.message
      )) {
        this.list.unshift(item);
        const listLevelSelected = this.getCheckedValues();
        if (listLevelSelected.length === 0 || listLevelSelected.includes(item.level)) {
          this.listFilter.unshift(item);
        }
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

  onClickOverNotifications(): void { this.stateOver = !this.stateOver; }
  onCheckboxChange() {
    const listLevelSelected = this.getCheckedValues();
    this.listFilter = this.list.filter(notification =>
      listLevelSelected.length === 0 || listLevelSelected.includes(notification.level)
    )
      .sort((a, b) => b.createdAt - a.createdAt);
  }

  getCheckedValues(): LevelType[] {
    return Object.keys(this.checkboxValues)
      .filter((key) => this.checkboxValues[key as LevelType])
      .map((key) => key as LevelType);
  }
}
