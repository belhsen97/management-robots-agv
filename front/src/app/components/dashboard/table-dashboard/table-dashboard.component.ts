import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { getValueSearchInput } from 'src/app/core/store/selectors/Global.selector';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';
import { Subscription, interval, throttleTime } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { GaugeChartComponent } from '../details/gauge-chart/gauge-chart.component';
import { refreshPannelRobot } from 'src/app/core/store/actions/Robot.Action';
import { GlobalState } from 'src/app/core/store/states/Global.state';

@Component({
  selector: 'app-table-dashboard',
  templateUrl: './table-dashboard.component.html',
  styleUrls: ['./table-dashboard.component.css']
})
export class TableDashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  private timerSubscription: Subscription | undefined;
  private getValueSearchInputSub !: Subscription | undefined;

  displayedColumns: string[] = ['name', 'view', 'statusRobot', 'modeRobot', 'connection', 'operationStatus', 'levelBattery', 'speed','distance','codeTag'];
  dataSource !: MatTableDataSource<RobotDto>;
  @ViewChild(MatSort) sort  !: MatSort;
  constructor(private storeGlobal: Store<GlobalState>,
    private storeRobot: Store<RobotState>,
    private dialog: MatDialog) { }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<RobotDto>();
    this.dataSource.data = robotState.listRobots;
    this.getValueSearchInputSub = this.storeGlobal.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSource == undefined) { return; }
      this.dataSource!.filter = value;
    });
 
  



    /*this.mqttClientService.curSubscription = this.mqttClientService.subscribe(mqttState.subscribe)?.subscribe((message: IMqttMessage) => {
      //console.log('MQTT --> Subscribe to topics res', message.payload.toString())
      
       const  updateRobot : RobotDto =JSON.parse(message.payload.toString());
      const index = robotState.listRobots.findIndex(robot => robot.name === updateRobot.name);
      if (index !== -1) {
        robotState.listRobots[index].connection =  updateRobot.connection;
        robotState.listRobots[index].levelBattery =  updateRobot.levelBattery; 
        robotState.listRobots[index].modeRobot =  updateRobot.modeRobot; 
        robotState.listRobots[index].operationStatus =  updateRobot.operationStatus; 
        robotState.listRobots[index].speed =  updateRobot.speed; 
        robotState.listRobots[index].statusRobot =  updateRobot.statusRobot; 
       }
   })*/
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
    this.timerSubscription = interval(1000).subscribe(() => {  
      this.dataSource.data = robotState.listRobots;
      this.storeRobot.dispatch(refreshPannelRobot());
    }); 
  }
  ngOnDestroy(): void {
    //this.mqttClientService.closeSubscribe( this.mqttClientService.curSubscription);
  
    this.timerSubscription!.unsubscribe();
    robotState.dataSource!.paginator = null;
    if (this.dataSource) { this.dataSource.disconnect(); }
    if (this.getValueSearchInputSub) { this.getValueSearchInputSub.unsubscribe(); } 
    

  }





  cellColorLevelBattery(value: number): string {
    return (value <= 15 ? 'cell-danger' : 'cell-steady');
  }
  cellColorSpeed(value: number): string {
    return (value > robotState.settingRobot!.speed.max || value < robotState.settingRobot!.speed.min ? 'cell-danger' : 'cell-steady');
  }


  openPopupRobotGuage(name: string = "") {
    const dialogRef = this.dialog.open(GaugeChartComponent, {
      //height: '500px',
      width: '400px',
      data: { name: name }
    });
    return dialogRef;
  }

}


