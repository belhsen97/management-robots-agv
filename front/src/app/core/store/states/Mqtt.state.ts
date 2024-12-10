import { environment } from 'src/environments/environment';
import { Publish } from '../models/Mqtt/Publish.model';
import { Subscribe } from '../models/Mqtt/Subscribe.model';
import { IMqttServiceOptions } from 'ngx-mqtt';
import { StatusClientMQTT } from '../models/Global/StatusClientMQTT.model';


export interface MQTTState {
    cnxClientConfig: IMqttServiceOptions;
    subscribes: any;
    publishs: any;
    qosList: ReadonlyArray<{ label: number; value: number }>;
    isConnection : boolean;
    subscribeSuccess : boolean;
    receiveNews : string;
    msgstatusClient?:StatusClientMQTT
}
const listSubscribes = {
    dataRobots : { topic: 'topic/data/robot/+', qos: 0 },
    dataPropertyRobot : { topic: 'topic/data/robot/+/property/+', qos: 0 },
    clientsStatus : { topic: '$SYS/brokers/+/clients/#', qos: 0 },
    notification : { topic: 'topic/data/service/notification/+/name/+', qos: 0 }
};
const listPublishs = {
    controleRobot : { topic: 'topic/control/robot/1', qos: 0, payload: '{ "msg": "Hello, I am browser." }' }
};
const connectionClientConfig: IMqttServiceOptions = environment.mqttClientConfig;
const qosList : readonly { label: number; value: number }[] =  [{ label: 0, value: 0 }, { label: 1, value: 1 }, { label: 2, value: 2 },];


export const mqttState: MQTTState = {
    subscribes: listSubscribes,
    publishs: listPublishs,
    cnxClientConfig: connectionClientConfig,
    qosList: qosList,
    isConnection : false,
    subscribeSuccess : false,
    receiveNews : ''
};

 