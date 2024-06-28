import { BodyContent } from "./BodyContent.model";
import { Recipient } from "./Recipent.model";
import { StatusMsg } from "./StatusMsg.enum";

export interface Msg { 
    id : String;
    subject: String;
    recipients : Recipient[];
    bodyContents : BodyContent[];
    timestamp : number ;
    status : StatusMsg;
}