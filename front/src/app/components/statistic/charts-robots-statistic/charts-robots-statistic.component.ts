import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts/highstock';



@Component({
  selector: 'app-charts-robots-statistic',
  templateUrl: './charts-robots-statistic.component.html',
  styleUrls: ['./charts-robots-statistic.component.css']
})
export class ChartsRobotsStatisticComponent  implements OnInit {
  ngOnInit(): void {
  }
  onClickStopAllRobot():void{
  }
  onClickStartAllRobot():void{
  }
  onClickTrunOffAllRobot():void{
  }
}