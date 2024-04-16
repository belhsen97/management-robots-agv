import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { getValueSearchInput } from 'src/app/core/store/selectors/global.Selectors';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { Publish } from 'src/app/core/store/models/Mqtt/Publish.model';
import { RobotService } from 'src/app/core/services/robot.service';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';
import { MqttClientService } from 'src/app/core/services/mqtt-client.service';
import { mqttState } from 'src/app/core/store/states/Mqtt.state';
import { IMqttMessage } from 'ngx-mqtt';
import { Subscription, interval, throttleTime } from 'rxjs';
import { StatusRobot } from 'src/app/core/store/models/Robot/StatusRobot.enum';
import { MatDialog } from '@angular/material/dialog';
import { GaugeChartComponent } from '../details/gauge-chart/gauge-chart.component';
import { getListRobot } from 'src/app/core/store/selectors/Robot.Selector';
import { refreshPannelRobot, refreshRobots, stopRefreshRobots } from 'src/app/core/store/actions/Robot.Action';

@Component({
  selector: 'app-table-dashboard',
  templateUrl: './table-dashboard.component.html',
  styleUrls: ['./table-dashboard.component.css']
})
export class TableDashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  private timerSubscription: Subscription | undefined;
  private getValueSearchInputSub !: Subscription | undefined;
  displayedColumns: string[] = ['name', 'view', 'statusRobot', 'modeRobot', 'connection', 'operationStatus', 'levelBattery', 'speed'];
  dataSource !: MatTableDataSource<RobotDto>;
  @ViewChild(MatSort) sort  !: MatSort;
  constructor(private store: Store,
    private storeRobot: Store<RobotState>,
    private mqttClientService: MqttClientService,
    private dialog: MatDialog) { }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<RobotDto>();
    this.dataSource.data = robotState.listRobots;
    this.getValueSearchInputSub = this.store.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSource == undefined) { return; }
      this.dataSource!.filter = value;
    });

    this.storeRobot.dispatch(refreshRobots({ subscribe: mqttState.subscribe }));

    this.storeRobot.select(getListRobot).pipe(throttleTime(1000)).subscribe(item => {
      robotState.listRobots = item;
      console.log("getListRobot")
      this.dataSource!.data = robotState.listRobots;
      this.storeRobot.dispatch(refreshPannelRobot());
    }
    );

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
    /*this.timerSubscription = interval(1000).subscribe(() => {  
      this.dataSource!.data = robotState.listRobots;
      this.storeRobot.dispatch(refreshPannelRobot());
    }); */
  }
  ngOnDestroy(): void {
    //this.mqttClientService.closeSubscribe( this.mqttClientService.curSubscription);
    this.storeRobot.dispatch(stopRefreshRobots());
    this.timerSubscription?.unsubscribe();
    if (this.dataSource) { this.dataSource.disconnect(); } //robotState.dataSource!.paginator = null;
    if (this.getValueSearchInputSub) { this.getValueSearchInputSub.unsubscribe(); }
  }



  onClickStopAllRobot(): void {
    const robot = { statusRobot: StatusRobot.INACTIVE };
    const publish: Publish = { topic: "topic/robot/control/all", qos: 0, payload: JSON.stringify(robot) };
    this.mqttClientService.publish(publish);
  }
  onClickStartAllRobot(): void {
    const robot = { statusRobot: StatusRobot.RUNNING };
    const publish: Publish = { topic: "topic/robot/control/all", qos: 0, payload: JSON.stringify(robot) };
    this.mqttClientService.publish(publish);
  }
  onClickTrunOffAllRobot(): void {

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


