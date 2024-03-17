import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service.ts.service';


@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit {
       
    constructor(public userService : UserService){}
    

   ngOnInit(): void {
        this.userService.userDto = this.userService.getUserDto(); 
           
   }

}