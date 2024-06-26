import { createFeatureSelector, createSelector } from "@ngrx/store";
import { RouterStateModel } from "../routers/DashbordRouterSerializer";
import {RouterReducerState} from '@ngrx/router-store'

const getrouterstate=createFeatureSelector<RouterReducerState<RouterStateModel>>('router');

export const getrouterinfo=createSelector(getrouterstate,(state)=>{
    return state.state;
})

export const getRouterId=createSelector(getrouterstate ,(state)=>{
    return state.state.params["id"];
})
export const getRouterName=createSelector(getrouterstate ,(state)=>{
    return state.state.params["name"];
})
