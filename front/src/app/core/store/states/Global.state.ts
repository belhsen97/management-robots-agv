import { MsgResponseStatus } from "../models/Global/MsgResponseStatus.model";
import { ReponseStatus } from "../models/Global/ReponseStatus.enum";

export interface GlobalState{
    IsOpen:boolean;
    keySearch:any;
    ReponseStatus: Readonly<{ successful: ReponseStatus; error: ReponseStatus; unsuccessful: ReponseStatus; }>;
    msgResponseStatus  : MsgResponseStatus;
    rangeDate:RangeDate;
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

export const globalState:GlobalState={
    IsOpen:false,
    keySearch: "",
    ReponseStatus: listResponseStatus,
    msgResponseStatus:msgReponseStatus,
    rangeDate : rangeDate
}
