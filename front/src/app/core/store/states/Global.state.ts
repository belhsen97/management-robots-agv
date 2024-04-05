import { MsgResponseStatus } from "../models/Global/MsgResponseStatus.model";
import { ReponseStatus } from "../models/Global/ReponseStatus.enum";

export interface GlobalState{
    IsOpen:boolean;
    keySearch:any;
    ReponseStatus: Readonly<{ successful: ReponseStatus; error: ReponseStatus; unsuccessful: ReponseStatus; }>;
    msgResponseStatus  : MsgResponseStatus;
}

const listResponseStatus: Readonly<{ successful: ReponseStatus; error: ReponseStatus; unsuccessful: ReponseStatus; }> =
 { successful: ReponseStatus.SUCCESSFUL,error: ReponseStatus.ERROR,unsuccessful: ReponseStatus.UNSUCCESSFUL};

var   defaultmsgReponseStatus:  MsgResponseStatus = { 
    title : "Error",   datestamp: new Date(),status : ReponseStatus.SUCCESSFUL , message : ""};
export const globalState:GlobalState={
    IsOpen:false,
    keySearch: "",
    ReponseStatus: listResponseStatus,
    msgResponseStatus:defaultmsgReponseStatus
}
