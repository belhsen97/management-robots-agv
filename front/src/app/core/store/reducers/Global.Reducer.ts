import { createReducer, on } from "@ngrx/store";
import { globalState } from "../states/Global.state"; 
import { ShowAlert, openSidebar, searchInput } from "../actions/Global.Action";

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


)

export function GlobalReducer(state: any, action: any) {
    return _GlobalReducer(state, action);}