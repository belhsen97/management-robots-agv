import { AppReducer } from "./Global/App.Reducer";
import {routerReducer} from '@ngrx/router-store'

export const AppState={
    app:AppReducer,
    router:routerReducer
}