import { Component } from '@angular/core';
import { MqttClientService } from 'src/app/core/services/mqtt-client.service';
import { Publish } from 'src/app/core/store/models/Mqtt/Publish.model';

@Component({
  selector: 'app-global-button-control',
  templateUrl: './global-button-control.component.html',
  styleUrls: ['./global-button-control.component.css']
})
export class GlobalButtonControlComponent {
  constructor(
    private mqttClientService: MqttClientService,
   ) { }


  
  onClickStop(): void {
    const publish: Publish = { topic: "topic/robot/control/all/OperationStatus/PAUSE", qos: 0, payload: "" };
    this.mqttClientService.publish(publish);
  }
  onClickStart(): void {
    const publish: Publish = { topic: "topic/robot/control/all/OperationStatus/NORMAL", qos: 0, payload: "" };
    this.mqttClientService.publish(publish);
  }
  onClickTrunOff(): void {

  }

}
