import { LevelType } from "./LevelType.enum";

export interface Sender {
    name:String;
    imageUrl?:String;
}
export interface Notification {
    sender:Sender;
    createdAt: Number;
    level:LevelType;//INFO, SUCCESS, WARNING, ERROR
    message: String;
}