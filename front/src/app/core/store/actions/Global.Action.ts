import { createAction, props } from "@ngrx/store"
import { MsgResponseStatus } from "../models/Global/MsgResponseStatus.model";
import { RangeDate } from "../states/Global.state";
import { Subscribe } from "../models/Mqtt/Subscribe.model";
import { Notification } from 'src/app/core/store/models/Notification/norifcation.models';


export const EmptyAction=createAction('[GLOBAL] Empty Action');
export const openSidebar=createAction('[GLOBAL] State Sidebar Dashbord Page',props<{ IsOpen:boolean}>());
export const ShowAlert=createAction('[GLOBAL] Show Alert',props<MsgResponseStatus>());
export const searchInput=createAction('[GLOBAL] On KeyUp Search Input',props<{ value:any}>());
export const searchInputRangeDate=createAction('[GLOBAL] Search By Range Dates Input',props<{ rangeDate : RangeDate }>());
export const searchInputRangeDateSuccess=createAction('[GLOBAL] Search Success By Range Dates inputs',props<{ rangeDate : RangeDate }>());


export const loadNotificationFail=createAction('[GLOBAL] Listener Notification Fail',props<{errorMessage:any}>());
export const loadAllNotificationsSuccess=createAction('[GLOBAL] Load All Notifications Success',props<{listNotifications:Notification[]}>());
export const loadAllNotifications=createAction('[GLOBAL] Load All Notifications');
export const loadNotificationSuccess=createAction('[GLOBAL] Load Notification Success',props<{notificationInput:Notification}>());
export const startListenerNotification=createAction('[GLOBAL] Start Listener Notification MQTT',props<{ subscribe: Subscribe }>());
export const stopListenerNotification=createAction('[GLOBAL] Stop Listener Notification MQTT');