import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { updateSettingRobot } from 'src/app/core/store/actions/Robot.Action';
import { RobotSettingDto } from 'src/app/core/store/models/Robot/RobotSettingDto.model';
import { getSettingRobot } from 'src/app/core/store/selectors/Robot.selector';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';

@Component({
  selector: 'app-global-setting',
  templateUrl: './global-setting.component.html',
  styleUrls: ['./global-setting.component.css']
})
export class GlobalSettingComponent implements OnInit, OnDestroy {
  public setting : RobotSettingDto = { distance: { min: 4, max: 5 }, speed: { min: 4, max: 8 } };
  private getSettingRobot: Subscription | undefined;
constructor( private storeRobot: Store<RobotState>){ }
  ngOnDestroy(): void { if (this.getSettingRobot) { this.getSettingRobot.unsubscribe(); }}
  ngOnInit(): void {
    this.getSettingRobot = this.storeRobot.select(getSettingRobot).subscribe(item => { 
        this.setting = item!;
    });
  }
 onSubmitForm(form: NgForm):void {
  if (form.valid){ this.storeRobot.dispatch(updateSettingRobot({ settinginput: this.setting })); } 
}


}
