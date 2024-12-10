import { MsgResponseStatus } from "../models/Global/MsgResponseStatus.model";
import { ReponseStatus } from "../models/Global/ReponseStatus.enum";
import { LevelType } from "../models/Notification/LevelType.enum";
import { Badge, Notification } from 'src/app/core/store/models/Notification/norifcation.models';

export interface GlobalState{
    IsOpen:boolean;
    keySearch:any;
    ReponseStatus: Readonly<{ successful: ReponseStatus; error: ReponseStatus; unsuccessful: ReponseStatus; }>;
    msgResponseStatus  : MsgResponseStatus;
    rangeDate:RangeDate;

    badge:Badge;
    notification?: Notification ;
    listNotifications: Notification[] ;
}
export interface RangeDate{
    start:Date;
    end:Date;
    limit:number;
}

const dateNow = new Date();
const lastDate = new Date();
const substructionTime:number  = 365 * 24 * 60 * 60 * 1000;
lastDate.setTime( dateNow.getTime() - substructionTime);
const rangeDate : RangeDate = {
    start:  lastDate ,
    end: dateNow,
    limit: dateNow.getTime()-lastDate.getTime()
};

const listResponseStatus: Readonly<{ successful: ReponseStatus; error: ReponseStatus; unsuccessful: ReponseStatus; }> ={ successful: ReponseStatus.SUCCESSFUL,error: ReponseStatus.ERROR,unsuccessful: ReponseStatus.UNSUCCESSFUL};

 const  msgReponseStatus:  MsgResponseStatus = { 
    title : "Error",
    datestamp: new Date(),
    status : ReponseStatus.SUCCESSFUL, 
    message : ""};






const notifications: Notification[] = [
        {
          senderName: 'John Doe',
          senderImageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941',
          createdAt: Date.now() - 100000,
          level: LevelType.INFO,
          message: 'John Doe liked your post.'
        },
        {
          senderName: 'John Doe',
          senderImageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941',
          createdAt: Date.now() - 500000,
          level: LevelType.WARNING,
          message: 'Jane Smith commented on your photo.'
        }
      ];
const badge : Badge = {color : "#d7dde1" , count :0};


export const globalState:GlobalState={
    IsOpen:false,
    keySearch: "",
    ReponseStatus: listResponseStatus,
    msgResponseStatus:msgReponseStatus,
    rangeDate : rangeDate,


    badge:badge ,
    //notification: notifications[0],
    listNotifications : notifications
}






















