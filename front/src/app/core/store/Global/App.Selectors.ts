import { createFeatureSelector, createSelector } from "@ngrx/store"; 
import { GlobalStateModel } from "../states/Gloabal.state";

const getAppstate=createFeatureSelector<GlobalStateModel>('app');

export const getStateSlidebare=createSelector(getAppstate,(state)=>{return state.IsOpen;});

export const getValueSearchInput=createSelector(getAppstate,(input)=>{return input.InputSearchValue;});
