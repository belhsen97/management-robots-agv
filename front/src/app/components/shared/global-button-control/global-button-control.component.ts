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
    const publish: Publish = { topic: "topic/control/driveless/robot/all/OPERATION_STATUS", qos: 0, payload: JSON.stringify({value:"PAUSE"}) };
    this.mqttClientService.publish(publish);
  }
  onClickStart(): void {
    const publish: Publish = { topic: "topic/control/driveless/robot/all/OPERATION_STATUS", qos: 0, payload: JSON.stringify({value:"NORMAL"}) };
    this.mqttClientService.publish(publish);
  }
  onClickTrunOff(): void {
    const publish: Publish = { topic: "topic/control/robot/all/property/STATUS", qos: 0, payload: JSON.stringify({value:"INACTIVE"}) };
    this.mqttClientService.publish(publish);
  }

}
