import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { MsgResponseStatus } from '../store/models/Global/MsgResponseStatus.model';
import { AuthenticationResponse } from '../store/models/User/AuthenticationResponse.model';
import { ReponseStatus } from '../store/models/Global/ReponseStatus.enum';
import { AuthenticationRequest } from '../store/models/User/AuthenticationRequest.model';
export class Service {




    protected url = `${environment.apiUrl}`;
    public msgResponseStatus  !: MsgResponseStatus ; 


    public authResponse : AuthenticationResponse ={title : "",datestamp : new Date(),status : ReponseStatus.ERROR,message : "",token: "" }
    public authRequest : AuthenticationRequest = {  username : "",  password : "" , email : "" };


    constructor(protected  http:HttpClient,
                private router: Router,
                private activeRoute: ActivatedRoute) {}
        
    goToComponent(component:string) : void {this.router.navigateByUrl(component);} // eq de routerLink="child1"
    public parseFormatDate (date:Date):string{
        return date.getFullYear() + "/" + (date.getMonth() + 1) +
        "/" + date.getDate() + " " + date.getHours() +
        ":" + date.getMinutes();
      }
      public toDate (str:string):Date{
        return new Date (str );
      }


      public formatDuration(ms: number): string {
        const seconds = Math.floor((ms / 1000) % 60);
        const minutes = Math.floor((ms / (1000 * 60)) % 60);
        const hours = Math.floor((ms / (1000 * 60 * 60)) % 24);
        const days = Math.floor(ms / (1000 * 60 * 60 * 24));
      
        const daysString = days > 0 ? `${days}d ` : '';
        const hoursString = hours > 0 ? `${hours}h ` : '';
        const minutesString = minutes > 0 ? `${minutes}m ` : '';
        const secondsString = `${seconds}s`;
      
        return daysString + hoursString + minutesString + secondsString;
      }

      public formatDateAgo(date: Date): string {
        const now = new Date();
        const timeDifference = now.getTime() - date.getTime();
      
        // Calculate time units
        const minutes = Math.floor(timeDifference / 60000); // 1 minute = 60000 milliseconds
        const hours = Math.floor(minutes / 60);
        const days = Math.floor(hours / 24);
      
        // Construct the formatted string
        let formattedDate = "";
      
        if (days > 0) {
          formattedDate += `${days} day${days > 1 ? 's' : ''} `;
        }
        
        const remainingHours = hours % 24;
        if (remainingHours > 0) {
          formattedDate += `${remainingHours} hour${remainingHours > 1 ? 's' : ''} `;
        }
      
        const remainingMinutes = minutes % 60;
        if (remainingMinutes > 0) {
          formattedDate += `${remainingMinutes} minute${remainingMinutes > 1 ? 's' : ''} `;
        }
      
        formattedDate += "ago";
      
        return formattedDate;
      }

      public  formatDate(date: Date): string {
        const year = date.getFullYear();
        const month = this.padZero(date.getMonth() + 1);
        const day = this.padZero(date.getDate());
        const hours = this.padZero(date.getHours());
        const minutes = this.padZero(date.getMinutes());
        const seconds = this.padZero(date.getSeconds());
      
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      }
      private  padZero(num: number): string {
        return num < 10 ? '0' + num : '' + num;
      }

   






      setToken(authResponse : AuthenticationResponse) :void
      {this.clearAuthenticationRequest();localStorage.setItem('AuthenticationResponse',JSON.stringify(authResponse));}  
      getAuthenticationRequest() : AuthenticationResponse{
        const authenticationResponseString =   localStorage.getItem('AuthenticationResponse'); 
        const authResponse = (  authenticationResponseString == null ?  this.authResponse :  JSON.parse(authenticationResponseString)  ) ;
        return authResponse;} 
      clearAuthenticationRequest() : void{localStorage.removeItem( 'AuthenticationResponse');}







      protected getRandomNumber(min: number, max: number): number {
        return Math.floor(Math.random() * (max - min)) + min;
      }
      protected getRandomEnumValue<T extends { [key: string]: string }>(enumeration: T): T[keyof T] {
        const enumKeys = Object.keys(enumeration) as Array<keyof T>;
        const randomKey = enumKeys[Math.floor(Math.random() * enumKeys.length)];
        return enumeration[randomKey];
      }
      protected generateRandomIP(): string {
        // IP address consists of 4 segments separated by dots
        const segment1 = this.getRandomNumber(0, 255);
        const segment2 = this.getRandomNumber(0, 255);
        const segment3 = this.getRandomNumber(0, 255);
        const segment4 = this.getRandomNumber(0, 255);
    
        return `${segment1}.${segment2}.${segment3}.${segment4}`;
      }





    
}