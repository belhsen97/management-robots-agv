import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'dateAgo' })
export class DateAgoPipe implements PipeTransform {
  transform(date: any): string {
    const now = new Date();
    const timeDifference = now.getTime() - ( typeof date == "number"  ? date :  date.getTime() );

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
    
    formattedDate += formattedDate == "" ? "now" : "ago";

    return formattedDate;
  }
  
}