export interface Notification {
    id: String;
    sender:String;
    createdAt: Number;
    expiredAt: Number;
    displayType: String;//"WEB" "MAIL" "WhatsApp"
    severity:String;//    INFO, SUCCESS, WARNING, ERROR
    description: String;
}
//Scheduling