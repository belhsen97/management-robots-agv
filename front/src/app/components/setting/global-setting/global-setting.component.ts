import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';

@Component({
  selector: 'app-global-setting',
  templateUrl: './global-setting.component.html',
  styleUrls: ['./global-setting.component.css']
})
export class GlobalSettingComponent {
  robotState !: RobotState;
constructor(){this.robotState  = robotState;}
 onSubmitForm(form: NgForm):void {
  console.log(form.invalid);
  console.log(this.robotState.settingRobot);
  }


}
