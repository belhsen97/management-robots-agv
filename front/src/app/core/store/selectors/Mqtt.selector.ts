import { createFeatureSelector, createSelector } from '@ngrx/store';
import { MQTTState } from '../states/Mqtt.state';
import { IMqttMessage } from 'ngx-mqtt';

const selectMQTTState=createFeatureSelector<MQTTState>('mqtt'); 

export const selectIsConnected = createSelector(selectMQTTState,(state : MQTTState)=>{ 
    return state.isConnection;
});
export const selectStatusClient = createSelector(selectMQTTState,(state : MQTTState)=>{ 
    return state.msgstatusClient;
});