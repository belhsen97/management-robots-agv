import { EventEmitter, Injectable } from '@angular/core';
import {IMqttMessage,IMqttServiceOptions,MqttService, IPublishOptions, IOnConnectEvent, IOnErrorEvent} from 'ngx-mqtt';
import { IClientSubscribeOptions } from 'mqtt-browser';
import { EMPTY, Observable, Observer, Subscription, of } from 'rxjs';
import { Publish } from '../store/models/Mqtt/Publish.model';
import { Subscribe } from '../store/models/Mqtt/Subscribe.model';
import { MQTTState, mqttState } from '../store/states/Mqtt.state';
import { Store } from '@ngrx/store';
import { connectionFailure, connectionSuccess } from '../store/actions/Mqtt.Action';
import { Packet } from 'mqtt-packet';
@Injectable({ providedIn: 'root' })
export class MqttClientService {  //   source : https://www.emqx.com/en/blog/how-to-use-mqtt-in-angular

  public curSubscription: Subscription | undefined;
  client: MqttService | undefined;


  constructor(private _mqttService: MqttService,  /*  private store: Store<MQTTState>*/) { this.client = this._mqttService; }

  connect(options: IMqttServiceOptions): Observable<void> {
    return new Observable(observer => {
      try { 
        this.client?.connect(options);
        observer.next();
        observer.complete();
      } catch (error) {
        observer.error(error);
      }
    });
  }
  disconnect() : Observable<void> {   return new Observable(observer => {
    try {
      this.client?.disconnect(true)
      observer.next();
      observer.complete();
      console.log('MQTT --> Successfully disconnected!')
    } catch (error: any) {
      console.log('MQTT --> Disconnect failed', error.toString())
      observer.error(error);
    }
  })}


  onConnect(): EventEmitter<IOnConnectEvent>{ return  this._mqttService.onConnect;}
  onClose(): EventEmitter<void>{ return  this._mqttService.onClose;}
  onMessage(): EventEmitter<Packet>{ return  this._mqttService.onMessage;}
  onError():  EventEmitter<IOnErrorEvent> { return  this._mqttService.onError;}
  
  publish(publish : Publish) {
    const { topic, qos, payload } = publish
    this.client?.unsafePublish(topic, payload, { qos } as IPublishOptions)
  }
  subscribe(subscribe: Subscribe): Observable<IMqttMessage> | undefined {
    const { topic, qos } = subscribe;
      return this.client?.observe(topic, { qos } as IClientSubscribeOptions);
  }
  closeSubscribe(curSubscription: Subscription | undefined) {
    curSubscription?.unsubscribe();
  }


  connectmqtt() {
    // Connection string, which allows the protocol to specify the connection method to be used
    // ws Unencrypted WebSocket connection
    // wss Encrypted WebSocket connection
    // mqtt Unencrypted TCP connection
    // mqtts Encrypted TCP connection
    try {
      this.client?.connect(mqttState.cnxClientConfig as IMqttServiceOptions)
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







}
