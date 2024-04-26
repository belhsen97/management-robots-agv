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

const rangeDate : RangeDate = {
    start: new Date(2020, 6, 1),
    end: new Date(2020, 6, 3),
    limit: new Date(2016, 6, 10).getTime()-new Date(2016, 6, 1).getTime()
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
