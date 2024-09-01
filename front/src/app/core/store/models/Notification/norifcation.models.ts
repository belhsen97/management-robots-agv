import { LevelType } from "./LevelType.enum";

export interface Notification {
    senderName:String;
    senderImageUrl?:String;
    createdAt: Number;
    level:LevelType;//INFO, SUCCESS, WARNING, ERROR
    message: String;
}