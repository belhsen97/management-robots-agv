import { createAction, props } from "@ngrx/store"
import { MsgResponseStatus } from "../models/Global/MsgResponseStatus.model";
import { RangeDate } from "../states/Global.state";

 
export const OPEN_SIDEBAR='[dashbord page] state sidebar'; 
export const SHOW_ALERT='[app event] show alert'
export const EMPTY_ACTION='[app event] empty'
export const ON_KEYUP_SEARCH_INPUT='[dashbord page] on keyup search input';
export const RANGE_DATE_SEARCH_INPUT='[dashbord page] search by range dates  input';
export const RANGE_DATE_SEARCH_INPUT_SUCCESS='[dashbord page] search success by range dates input';

export const EmptyAction=createAction(EMPTY_ACTION)
export const openSidebar=createAction(OPEN_SIDEBAR,props<{ IsOpen:boolean}>());
export const ShowAlert=createAction(SHOW_ALERT,props<MsgResponseStatus>());
export const searchInput=createAction(ON_KEYUP_SEARCH_INPUT,props<{ value:any}>());
export const searchInputRangeDate=createAction(RANGE_DATE_SEARCH_INPUT,props<{ rangeDate : RangeDate }>());
export const searchInputRangeDateSuccess=createAction(RANGE_DATE_SEARCH_INPUT_SUCCESS,props<{ rangeDate : RangeDate }>());