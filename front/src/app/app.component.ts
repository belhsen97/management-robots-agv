import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { MqttClientService } from './core/services/mqtt-client.service';
import {  MQTTState, mqttState } from './core/store/states/Mqtt.state';
import { Store } from '@ngrx/store';
import { subscribeStatusClients } from './core/store/actions/Mqtt.Action';
import { Subscription } from 'rxjs';
import { selectStatusClient } from './core/store/selectors/Mqtt.selector';
import { RobotService } from './core/services/robot.service';
import { RobotState, robotState } from './core/store/states/Robot.state';
import { loadAllNotifications, ShowAlert, startListenerNotification, stopListenerNotification } from './core/store/actions/Global.Action';
import { ReponseStatus } from './core/store/models/Global/ReponseStatus.enum';
import { loadRobots, loadSettingRobot, startListenerAllRobots, startListenerAllRobotsByProperty, stopListenerAllRobots, stopListenerAllRobotsByProperty } from './core/store/actions/Robot.Action';
import { getListRobot, getSettingRobot } from './core/store/selectors/Robot.selector';
import { TagService } from './core/services/tag.service';
import { tagState } from './core/store/states/Tag.state';
import { GlobalState } from './core/store/states/Global.state';
// build + deploy https://v17.angular.io/guide/deployment#routed-apps-must-fallback-to-indexhtml
//https://scalabledeveloper.com/posts/angularjs-html5-mode-or-pretty-urls-on-apache-using-htaccess/
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit , OnDestroy {

  private getListRobotSub !: Subscription | undefined;
  private getStatusClientSub !: Subscription | undefined;
  private  getSettingRobotSub !: Subscription | undefined;
  
  constructor( private tagService: TagService,
    private mqttClientService: MqttClientService,
    public  robotService: RobotService,
    private storeMqtt: Store<MQTTState>,
    private storeRobot: Store<RobotState>,
    private storeGlobal: Store<GlobalState>){ }

  ngOnInit(): void {
    this.getListRobotSub = this.storeRobot.select(getListRobot).subscribe(item => {
      robotState.listRobots = item;
    });
    this.getSettingRobotSub = this.storeRobot.select(getSettingRobot).subscribe(item => {
      robotState.settingRobot = item;
    });
    this.getStatusClientSub= this.storeMqtt.select(selectStatusClient).subscribe(item => {
       console.log(item);
    });

    this.tagService.getAll().subscribe(
      (response) => {
        tagState.listTags = response.body;
      }
      , (error) => {
        this.storeGlobal.dispatch(ShowAlert({ title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message } ));
        //this.workstationService.goToComponent("/sign-in");
      });
  }

  ngAfterViewInit(): void {
    this.storeRobot.dispatch(loadSettingRobot());
    this.storeRobot.dispatch(loadRobots());
    this.storeRobot.dispatch(startListenerAllRobots({ subscribe: mqttState.subscribes.dataRobots }));
    this.storeRobot.dispatch(startListenerAllRobotsByProperty({ subscribe: mqttState.subscribes.dataPropertyRobot }));
    this.storeMqtt.dispatch(subscribeStatusClients({ subscribe: mqttState.subscribes.clientsStatus }));

    this.storeGlobal.dispatch(loadAllNotifications());
    this.storeGlobal.dispatch(startListenerNotification({ subscribe: mqttState.subscribes.notification }));
  }

  ngOnDestroy(): void {
    this.storeRobot.dispatch(stopListenerAllRobots());
    this.storeRobot.dispatch(stopListenerAllRobotsByProperty()); 
    this.storeGlobal.dispatch(stopListenerNotification()); 
    console.log("ngOnDestroy"); 
    if (this.getListRobotSub) { this.getListRobotSub.unsubscribe(); }
    if (this.getStatusClientSub) { this.getStatusClientSub.unsubscribe(); }
    if (this.getSettingRobotSub) { this.getSettingRobotSub.unsubscribe(); }
    this.mqttClientService.disconnect();
  }

}







    
    // isConnectedsubscribe !: Subscription;
    // onSubcribe !: Subscription;
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