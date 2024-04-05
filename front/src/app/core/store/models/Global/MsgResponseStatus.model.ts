import { ReponseStatus } from "./ReponseStatus.enum";
export interface MsgResponseStatus { 
    title : string ;
    datestamp : Date ;
    status : ReponseStatus ;
    message : string ;
}