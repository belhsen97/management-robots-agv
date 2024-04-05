import { createReducer, on } from '@ngrx/store';
import { MQTTState, mqttState } from '../states/Mqtt.state';
import {  connectMQTT, connectionClosed, connectionFailure, connectionSuccess,     onSubscribeMQTT,     receiveMessage, subscribeFailure,  subscribeSuccess,  updateMQTTState } from '../actions/Mqtt.Action';




const _mqttReducer = createReducer(mqttState, 

    on(connectionFailure, (state, { error }) => ({ ...state, isConnection: false, error })),
    on(connectionSuccess, state => ({
       ...state, isConnection: true 
      })),
      on(connectionClosed, state => ({
        ...state, isConnection: false
       })),

       on(onSubscribeMQTT,(state,action)=>{ 
        return{
            ...state,
            msg: action.msg
        }
    }),

  );
export function mqttReducer (state: any, action: any) {
    return _mqttReducer(state, action);

}