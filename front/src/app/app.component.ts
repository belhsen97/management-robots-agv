import { Component, OnDestroy, OnInit } from '@angular/core';
import { MqttClientService } from './core/services/mqtt-client.service';
import { UserService } from './core/services/user.service.ts.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {

  title = 'dashboard-robot-agv';

  constructor(public mqttClientService: MqttClientService,public userService: UserService) { }

  ngOnInit(): void {
    this.userService.userDto = this.userService.getUserDto();
    // this.mqttClientService.createConnection();
    //this.mqttClientService.subscribe();
    //this.mqttClientService.publish();
  }

  ngOnDestroy(): void {
    this.mqttClientService.closeSubscribe();
    this.mqttClientService.disconnect();
  }

}
