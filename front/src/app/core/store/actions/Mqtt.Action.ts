import { createAction, props } from '@ngrx/store';
import { IMqttMessage, IMqttServiceOptions } from 'ngx-mqtt';
import { Publish } from '../models/Mqtt/Publish.model';
import { MQTTState } from '../states/Mqtt.state';
import { Subscribe } from '../models/Mqtt/Subscribe.model';
import { Observable } from 'rxjs';




export const connectMQTT = createAction('[MQTT] Connect', props<{ options: IMqttServiceOptions }>());
export const desconnectMQTT = createAction('[MQTT]  Desconnect');
export const connectionSuccess = createAction('[MQTT] Connection Success');
export const connectionFailure = createAction('[MQTT] Connection Failure', props<{ error: any }>());
export const connectionClosed = createAction('[MQTT] Connection Close');



export const subscribeMQTT = createAction('[MQTT] Subscribe', props<{ subscribe: Subscribe }>());
export const onSubscribeMQTT = createAction('[MQTT] Subscribe', props<{ msg: IMqttMessage }>());

export const updateMQTTState = createAction('[MQTT] Update State', props<{ state: MQTTState }>());
export const subscribeSuccess = createAction('[MQTT] Subscribe Success');
export const subscribeFailure = createAction('[MQTT] Subscribe Failure', props<{ error: any }>());
export const receiveMessage = createAction('[MQTT] Receive Message', props<{ message: string }>());
