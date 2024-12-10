import { createReducer, on } from "@ngrx/store";
import { RangeDate, globalState } from "../states/Global.state"; 
import { ShowAlert, loadNotificationSuccess, loadNotificationFail, openSidebar, searchInput, searchInputRangeDateSuccess, loadAllNotificationsSuccess } from "../actions/Global.Action";

const _GlobalReducer  = createReducer(globalState,
    on(openSidebar, (state, action) => {
        return {
            ...state,
            IsOpen: action.IsOpen
        }
    })
    ,on(searchInput, (state, action) => {
   
        return {
            ...state,
            keySearch: action.value
        }
    }) 
    ,on(searchInputRangeDateSuccess, (state, action) => {
        const rangeDate : RangeDate = {start: action.rangeDate.start, end: action.rangeDate.end,
            limit: action.rangeDate.limit };
        return {
            ...state,
            rangeDate:rangeDate
        }
    }) ,


    on(loadNotificationSuccess, (state, action) => {
        return {
            ...state,
            notification: action.notificationInput
        }
    }),
    on(loadAllNotificationsSuccess, (state, action) => {

        return {
            ...state,
            listNotifications: action.listNotifications
        }
    }),
    on(loadNotificationFail, (state, action) => {
        return {
            ...state,
            errorMessage: action.errorMessage.message
        }
    }),



)

export function GlobalReducer(state: any, action: any) {return _GlobalReducer(state, action);}