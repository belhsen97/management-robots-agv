import { Msg } from "../models/mail/Msg.model";
import { StatusMsg } from "../models/mail/StatusMsg.enum";
import { TypeBody } from "../models/mail/TypeBody.enum";

export interface MailState {
    typeBody?: ReadonlyArray<{ label: string; value: TypeBody }>;
    listAddress:String[]
    recipients:{to:String[],cc:String[],bcc:String[]};
    msg: Msg;
    listMsg?:Msg[];
    errorMessage?:String;
}

const ListTypeBody: readonly { label: string; value: TypeBody }[] = [
    { label: 'PDF', value: TypeBody.PDF },
    { label: 'JPEG', value: TypeBody.JPEG },
    { label: 'PNG', value: TypeBody.PNG },
    { label: 'GIF', value: TypeBody.GIF },
    { label: 'WEBP', value: TypeBody.WEBP },
    { label: 'AVIF', value: TypeBody.AVIF },
    { label: 'DOC', value: TypeBody.DOC },
    { label: 'XLS', value: TypeBody.XLS },
    { label: 'TXT', value: TypeBody.TXT },
    { label: 'HTML', value: TypeBody.HTML },
];

const defaultMsg : Msg = { 
    id : "0",
    subject: "",
    recipients : [],
    bodyContents : [],
    timestamp : 0,//new Date(), 
    status : StatusMsg.QUEUED
}
    
export const mailState: MailState = {
    typeBody : ListTypeBody,
    listAddress : ['Apple@gmail.com', 'Lemon@gmail.com', 'Lime@gmail.com', 'Orange@gmail.com', 'Strawberry@gmail.com'],
    recipients:{to:[],cc:[],bcc:[]},
    msg:defaultMsg,
    listMsg:[],
    errorMessage:""
};