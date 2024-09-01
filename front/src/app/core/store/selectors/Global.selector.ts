import { createFeatureSelector, createSelector } from "@ngrx/store"; 
import { GlobalState } from "../states/Global.state";

const getAppstate=createFeatureSelector<GlobalState>('global');

export const getStateSlidebare=createSelector(getAppstate,(state)=>{return state.IsOpen;});
export const getValueSearchInput=createSelector(getAppstate,(input)=>{return input.keySearch;});
export const getDateRangeSearchInput=createSelector(getAppstate,(state)=>{return state.rangeDate;});

export const getNotification=createSelector(getAppstate,(state)=>{return state.notification;});
export const getListNotifications=createSelector(getAppstate,(state)=>{return state.listNotifications;});