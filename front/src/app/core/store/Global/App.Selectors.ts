import { createFeatureSelector, createSelector } from "@ngrx/store"; 
import { AppStateModel } from "./AppState.Model";

const getAppstate=createFeatureSelector<AppStateModel>('app');

export const getStateSlidebare=createSelector(getAppstate,(state)=>{return state.IsOpen;});

export const getValueSearchInput=createSelector(getAppstate,(input)=>{return input.InputSearchValue;});
