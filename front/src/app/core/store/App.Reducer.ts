import { GlobalReducer } from "./reducers/Global.Reducer";
import {routerReducer} from '@ngrx/router-store'
import { mqttReducer } from "./reducers/Mqtt.Reducer";
//import { mqttReducer } from "./reducers/Mqtt.Reducer";

export const AppReducer={
    global:GlobalReducer,
    router:routerReducer,
   // mqtt:mqttReducer
}