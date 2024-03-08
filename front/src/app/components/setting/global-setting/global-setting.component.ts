import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { RobotService } from 'src/app/core/store/services/robot.service';

@Component({
  selector: 'app-global-setting',
  templateUrl: './global-setting.component.html',
  styleUrls: ['./global-setting.component.css']
})
export class GlobalSettingComponent {

constructor(public robotService: RobotService){

}
 onSubmitForm(form: NgForm):void {
  console.log(form.invalid);
  console.log(this.robotService.settingRobot);
  }


}
