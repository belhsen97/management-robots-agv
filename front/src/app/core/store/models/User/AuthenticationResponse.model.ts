import { MsgResponseStatus } from "../Global/MsgResponseStatus.model";

export interface AuthenticationResponse extends MsgResponseStatus{
    token : string; 
}