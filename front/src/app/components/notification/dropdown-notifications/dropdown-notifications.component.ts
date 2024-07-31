import { Component } from '@angular/core';
import { LevelType } from 'src/app/core/store/models/Notification/LevelType.enum';
import { Notification } from 'src/app/core/store/models/Notification/norifcation.models';

@Component({
  selector: 'app-dropdown-notifications',
  templateUrl: './dropdown-notifications.component.html',
  styleUrls: ['./dropdown-notifications.component.css']
})
export class DropdownNotificationsComponent {

  stopPropagation(event: Event): void {
    event.stopPropagation();
  }


  public stateOver = true;
 onClickOverNotifications():void{
  this.stateOver = !this.stateOver;
 }

   public notifications: Notification[] = [
    {
      sender: {
        name: 'John Doe',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 100000,
      level: LevelType.INFO,
      message: 'John Doe liked your post. dfe erfe  er ef'
    },
    {
      sender: {
        name: 'Jane Smith',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 500000,
      level: LevelType.WARNING,
      message: 'Jane Smith commented on your photo.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    },
    {
      sender: {
        name: 'Mike Johnson',
        imageUrl: 'https://wac-cdn.atlassian.com/fr/dam/jcr:ba03a215-2f45-40f5-8540-b2015223c918/Max-R_Headshot%20(1).jpg?cdnVersion=1941'
      },
      createdAt: Date.now() - 1000000,
      level: LevelType.ERROR,
      message: 'Mike Johnson sent you a friend request.'
    }
  ];
}
