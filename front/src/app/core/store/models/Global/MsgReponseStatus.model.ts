import { ReponseStatus } from "./ReponseStatus.enum";
export interface MsgReponseStatus { 
    title : string ;
    datestamp : Date ;
    status : ReponseStatus ;
    message : string ;
}