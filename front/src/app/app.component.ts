import { Component, OnDestroy, OnInit } from '@angular/core';
import { MqttClientService } from './core/services/mqtt-client.service';
import { UserService } from './core/services/user.service.ts.service';
import { userState } from './core/store/states/User.state';
import {  MQTTState, mqttState } from './core/store/states/Mqtt.state';
import { IMqttMessage } from 'ngx-mqtt';
import { Store } from '@ngrx/store';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { connectMQTT, connectionSuccess, desconnectMQTT, onSubscribeMQTT, subscribeMQTT } from './core/store/actions/Mqtt.Action';
import { state } from '@angular/animations';
import { Subscription } from 'rxjs';
import {  selectIsConnected } from './core/store/selectors/Mqtt.selector';
import * as mqtt from 'mqtt';
import { environment } from 'src/environments/environment';
import { RobotService } from './core/services/robot.service';
import { RobotState, robotState } from './core/store/states/Robot.state';
import { ShowAlert } from './core/store/actions/Global.Action';
import { ReponseStatus } from './core/store/models/Global/ReponseStatus.enum';
import { loadRobots, loadSettingRobot, refreshRobots } from './core/store/actions/Robot.Action';
import { getListRobot } from './core/store/selectors/Robot.Selector';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {

  title = 'dashboard-robot-agv';
robot !: RobotDto ; 
      // isConnectedsubscribe !: Subscription;
      // onSubcribe !: Subscription;

  constructor(public mqttClientService: MqttClientService,public userService: UserService,
    public robotService: RobotService,
    private storeMqtt: Store<MQTTState>,
    private storeRobot: Store<RobotState>
  ) 
  { }

  ngOnInit(): void {
    this.storeRobot.dispatch(loadRobots());
    this.storeRobot.dispatch(loadSettingRobot());
     userState.userDto = this.userService.getUserDto();
    // this.robotService.getAll().subscribe(
    //   (response) => { 
    //      robotState.listRobots = response.body;
    //      robotState.listRobots.forEach(r => r.createdAt = this.robotService.toDate(r.createdAt.toString()));
    //      robotState.dataSource!.data = robotState.listRobots ;
    //   }
    //    ,(error) => {
    //      this.robotService.msgResponseStatus  =   { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
    //      this.store.dispatch( ShowAlert(  this.robotService.msgResponseStatus ) ); 
    //    }) ;

    // this.  mqttClientService.connect(mqttState.cnxClientConfig);





    // this.isConnectedsubscribe = this.store.select(selectIsConnected).subscribe(data => {
    //   console.log("selectIsConnected");
    //   console.log(data);
    //  });
    //  this.onSubcribe = this.store.select(selectBookState).subscribe(message => {
  
    //   console.log("selectBookState");
    //   console.log('MQTT --> Subscribe to topics res', message.payload.toString())
    //  });

    //  this.storeRobot.dispatch(refreshRobot({subscribe:mqttState.subscribe}));


    //this.store.dispatch(desconnectMQTT());

 /*this.  mqttClientService.subscribe(mqttState.subscribe) ;*/
   // this.store.dispatch(connectMQTT({options:mqttState.cnxClientConfig}) ); 
    //this.store.dispatch(subscribeMQTT({subscribe:mqttState.subscribe}) ); 
    //this.store.dispatch(connectionSuccess());
   // this.mqttClientService.connectClient(mqttState.cnxClientConfig);
    //this.mqttClientService.onConnect();
    //this.mqttClientService.subscribe();
    // mqttState.subscribe = { topic: 'topic/robot/#', qos: 0 };
    // this.mqttClientService.curSubscription =  
    //  this.mqttClientService.subscribe(mqttState.subscribe).subscribe((message: IMqttMessage) => {
    //        mqttState.subscribeSuccess = true
    //        console.log('MQTT --> Subscribe to topics res', message.payload.toString())
    //      });
    // this.mqttClientService.publish(mqttState.publish);
  }

  ngOnDestroy(): void {
    this.mqttClientService.disconnect(); 
  }

}
