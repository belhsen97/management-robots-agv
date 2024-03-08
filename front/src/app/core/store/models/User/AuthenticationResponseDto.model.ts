import { MsgReponseStatus } from "../Global/MsgReponseStatus.model";

export interface AuthenticationResponseDto extends MsgReponseStatus{
    token : string; 
}