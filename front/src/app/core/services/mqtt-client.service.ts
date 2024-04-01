import { Injectable } from '@angular/core';
import {IMqttMessage,IMqttServiceOptions,MqttService, IPublishOptions} from 'ngx-mqtt';
import { IClientSubscribeOptions } from 'mqtt-browser';
import { Subscription } from 'rxjs';
import { Publish } from '../store/models/Mqtt/Publish.model';
import { Subscribe } from '../store/models/Mqtt/Subscribe.model';
import { mqttState } from '../store/states/Mqtt.state';

@Injectable({ providedIn: 'root' })
export class MqttClientService {  //   source : https://www.emqx.com/en/blog/how-to-use-mqtt-in-angular

  private curSubscription: Subscription | undefined;
  client: MqttService | undefined;


  constructor(private _mqttService: MqttService) { this.client = this._mqttService; }





  connect() {
    // Connection string, which allows the protocol to specify the connection method to be used
    // ws Unencrypted WebSocket connection
    // wss Encrypted WebSocket connection
    // mqtt Unencrypted TCP connection
    // mqtts Encrypted TCP connection
    try {
      this.client?.connect(mqttState.connection as IMqttServiceOptions)
    } catch (error) {
      console.log('MQTT --> mqtt.connect error', error);
    }
    this.client?.onConnect.subscribe(() => {
      mqttState.isConnection = true
      console.log('MQTT --> Connection succeeded!');
    });
    this.client?.onError.subscribe((error: any) => {
      mqttState.isConnection = false
      console.log('CMQTT --> onnection failed', error);
    });
    this.client?.onMessage.subscribe((packet: any) => {
      mqttState.receiveNews = mqttState.receiveNews.concat(packet.payload.toString())
      console.log(`MQTT --> Received message ${packet.payload.toString()} from topic ${packet.topic}`)
    })
  }

  subscribe(subscribe : Subscribe) {
    const { topic, qos } = subscribe
    this.curSubscription = this.client?.observe(topic, { qos } as IClientSubscribeOptions).subscribe((message: IMqttMessage) => {
      mqttState.subscribeSuccess = true
      console.log('MQTT --> Subscribe to topics res', message.payload.toString())
    })
  }

  closeSubscribe() {this.curSubscription?.unsubscribe(); mqttState.subscribeSuccess = false;}


  publish(publish : Publish) {
    const { topic, qos, payload } = publish
    this.client?.unsafePublish(topic, payload, { qos } as IPublishOptions)
  }


  disconnect() {
    try {
      this.client?.disconnect(true)
      mqttState.isConnection = false
      console.log('MQTT --> Successfully disconnected!')
    } catch (error: any) {
      console.log('MQTT --> Disconnect failed', error.toString())
    }
  }

}
