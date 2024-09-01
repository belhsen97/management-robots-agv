import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { CountRobotsProperties } from 'src/app/core/store/models/Robot/CountRobotsProperties.model';
import { getCountRobotsProperties } from 'src/app/core/store/selectors/Robot.selector';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';

@Component({
  selector: 'app-panel-robot',
  templateUrl: './panel-robot.component.html',
  styleUrls: ['./panel-robot.component.css']
})
export class PanelRobotComponent  implements OnInit , OnDestroy {
  robotsCount !: CountRobotsProperties;
  private getCountRobotsPropertiestSub !: Subscription | undefined;
  constructor(private storeRobot: Store<RobotState>) {  this.robotsCount  = robotState.count!;}
  ngOnInit(): void {
   this.getCountRobotsPropertiestSub = this.storeRobot.select(getCountRobotsProperties).subscribe(item => {   this.robotsCount = item!; });
  }
  ngOnDestroy() {if (this.getCountRobotsPropertiestSub) {this.getCountRobotsPropertiestSub.unsubscribe();}}
}
