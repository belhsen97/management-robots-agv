import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Role } from 'src/app/core/store/models/User/Role.enum';
import { UserService } from 'src/app/core/services/user.service.ts.service';

@Component({
  selector: 'app-update-role',
  templateUrl: './update-role.component.html',
  styleUrls: ['./update-role.component.css']
})
export class UpdateRoleComponent implements OnInit {
  @Input() state : boolean = false;
  @Output() onYesNoEvent=new EventEmitter<boolean>();
  @Input() currentRole! : Role;
  @Output() choiseRole =new EventEmitter<Role>(); 
  constructor(public userService : UserService) {}
  ngOnInit(): void {}
  onClickYesNo(state : boolean):void {  this.state = state; this.onYesNoEvent.emit(this.state);  }
  onClickToSend():void {this.choiseRole.emit(this.currentRole); this.state = false; this.onYesNoEvent.emit(this.state); }
}