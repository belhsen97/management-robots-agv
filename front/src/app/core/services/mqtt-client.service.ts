import { Injectable } from '@angular/core';
import {IMqttMessage,IMqttServiceOptions,MqttService, IPublishOptions} from 'ngx-mqtt';
import { IClientSubscribeOptions } from 'mqtt-browser';
import { Subscription } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class MqttClientService {  //   source : https://www.emqx.com/en/blog/how-to-use-mqtt-in-angular

  private curSubscription: Subscription | undefined;
  client: MqttService | undefined;
  isConnection = false;
  subscribeSuccess = false;
  private connection = environment.mqttClientConfig;

  constructor(private _mqttService: MqttService) { this.client = this._mqttService; }



  subscription = {
    topic: 'topic/robot/#',
    qos: 0,
  };

  publish = {
    topic: 'topic/robot/1',
    qos: 0,
    payload: '{ "msg": "Hello, I am browser." }',
  };
  receiveNews = '';
  qosList = [{ label: 0, value: 0 }, { label: 1, value: 1 }, { label: 2, value: 2 },];

  createConnection() {
    // Connection string, which allows the protocol to specify the connection method to be used
    // ws Unencrypted WebSocket connection
    // wss Encrypted WebSocket connection
    // mqtt Unencrypted TCP connection
    // mqtts Encrypted TCP connection
    try {
      this.client?.connect(this.connection as IMqttServiceOptions)
    } catch (error) {
      console.log('MQTT --> mqtt.connect error', error);
    }
    this.client?.onConnect.subscribe(() => {
      this.isConnection = true
      console.log('MQTT --> Connection succeeded!');
    });
    this.client?.onError.subscribe((error: any) => {
      this.isConnection = false
      console.log('CMQTT --> onnection failed', error);
    });
    this.client?.onMessage.subscribe((packet: any) => {
      this.receiveNews = this.receiveNews.concat(packet.payload.toString())
      console.log(`MQTT --> Received message ${packet.payload.toString()} from topic ${packet.topic}`)
    })
  }

  doSubscribe() {
    const { topic, qos } = this.subscription
    this.curSubscription = this.client?.observe(topic, { qos } as IClientSubscribeOptions).subscribe((message: IMqttMessage) => {
      this.subscribeSuccess = true
      console.log('MQTT --> Subscribe to topics res', message.payload.toString())
    })
  }


  doUnSubscribe() {
    this.curSubscription?.unsubscribe()
    this.subscribeSuccess = false
  }


  doPublish() {
    const { topic, qos, payload } = this.publish
    console.log(this.publish)
    this.client?.unsafePublish(topic, payload, { qos } as IPublishOptions)
  }


  destroyConnection() {
    try {
      this.client?.disconnect(true)
      this.isConnection = false
      console.log('MQTT --> Successfully disconnected!')
    } catch (error: any) {
      console.log('MQTT --> Disconnect failed', error.toString())
    }
  }



}
