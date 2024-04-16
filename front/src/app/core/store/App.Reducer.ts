import { GlobalReducer } from "./reducers/Global.Reducer";
import {routerReducer} from '@ngrx/router-store'
import { mqttReducer } from "./reducers/Mqtt.Reducer";
import { robotReducer } from "./reducers/Robot.Reducer";
//import { mqttReducer } from "./reducers/Mqtt.Reducer";

export const AppReducer={
    global:GlobalReducer,
    router:routerReducer,
    robotstate : robotReducer,
 //   mqtt:mqttReducer
}