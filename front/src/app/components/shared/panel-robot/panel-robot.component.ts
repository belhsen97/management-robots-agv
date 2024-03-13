import { Component } from '@angular/core';
import { RobotService } from 'src/app/core/services/robot.service';

@Component({
  selector: 'app-panel-robot',
  templateUrl: './panel-robot.component.html',
  styleUrls: ['./panel-robot.component.css']
})
export class PanelRobotComponent {
  constructor(public  robotService : RobotService) {}
}
