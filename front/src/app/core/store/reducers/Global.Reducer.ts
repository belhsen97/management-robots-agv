import { createReducer, on } from "@ngrx/store";
import { RangeDate, globalState } from "../states/Global.state"; 
import { ShowAlert, openSidebar, searchInput, searchInputRangeDateSuccess } from "../actions/Global.Action";

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
    ,on(/*searchInputRangeDate*/searchInputRangeDateSuccess, (state, action) => {
        const rangeDate : RangeDate = {start: action.rangeDate.start, end: action.rangeDate.end,
            limit: action.rangeDate.limit };
        return {
            ...state,
            rangeDate:rangeDate
        }
    }) 

)

export function GlobalReducer(state: any, action: any) {
    return _GlobalReducer(state, action);}