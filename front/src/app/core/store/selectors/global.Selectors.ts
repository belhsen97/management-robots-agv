import { createFeatureSelector, createSelector } from "@ngrx/store"; 
import { GlobalState } from "../states/Global.state";

const getAppstate=createFeatureSelector<GlobalState>('global');

export const getStateSlidebare=createSelector(getAppstate,(state)=>{return state.IsOpen;});

export const getValueSearchInput=createSelector(getAppstate,(input)=>{return input.keySearch;});
