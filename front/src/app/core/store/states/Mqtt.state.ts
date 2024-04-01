import { environment } from 'src/environments/environment';
import { Publish } from '../models/Mqtt/Publish.model';
import { Subscribe } from '../models/Mqtt/Subscribe.model';
import { IMqttServiceOptions } from 'ngx-mqtt';


export interface MQTTState {
    subscribe: Subscribe;
    publish: Publish;
    connection: IMqttServiceOptions;
    qosList: ReadonlyArray<{ label: number; value: number }>;
    isConnection : boolean;
    subscribeSuccess : boolean;
    receiveNews : String;
}



const subscribe : Subscribe = { topic: 'topic/robot/#', qos: 0 };
const publish: Publish = { topic: 'topic/robot/1', qos: 0, payload: '{ "msg": "Hello, I am browser." }' };
const connection: IMqttServiceOptions = environment.mqttClientConfig;
const qosList : readonly { label: number; value: number }[] =  [{ label: 0, value: 0 }, { label: 1, value: 1 }, { label: 2, value: 2 },];


export const mqttState: MQTTState = {
    subscribe: subscribe,
    publish: publish,
    connection: connection,
    qosList: qosList,
    isConnection : false,
    subscribeSuccess : false,
    receiveNews : ''
};