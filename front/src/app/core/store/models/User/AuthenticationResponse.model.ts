import { MsgReponseStatus } from "../Global/MsgReponseStatus.model";

export interface AuthenticationResponse extends MsgReponseStatus{
    token : string; 
}