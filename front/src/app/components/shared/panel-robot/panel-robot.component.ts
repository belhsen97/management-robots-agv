import { Component } from '@angular/core';
import { RobotService } from 'src/app/core/services/robot.service';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';

@Component({
  selector: 'app-panel-robot',
  templateUrl: './panel-robot.component.html',
  styleUrls: ['./panel-robot.component.css']
})
export class PanelRobotComponent {
  robotState !: RobotState;
  constructor() {  this.robotState  = robotState;}
}
