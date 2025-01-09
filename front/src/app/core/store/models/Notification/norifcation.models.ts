import { LevelType } from "./LevelType.enum";

export interface Notification {
    senderName:String;
    senderImageUrl?:String;
    createdAt: number;
    level:LevelType;//INFO, SUCCESS, WARNING, ERROR
    message: String;
}
export interface Badge {
    color:String;
    count: number;
}