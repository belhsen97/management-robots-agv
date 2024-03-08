import { createReducer, on } from "@ngrx/store";
import { GlobalState } from "./Gloabal.state"; 
import { ShowAlert, openSidebar, searchInput } from "./App.Action";

const _AppReducer = createReducer(GlobalState,
    on(openSidebar, (state, action) => {
        return {
            ...state,
            IsOpen: action.IsOpen
        }
    })
    ,on(searchInput, (state, action) => {
        return {
            ...state,
            InputSearchValue: action.value
        }
    }) 


)

export function AppReducer(state: any, action: any) {
    return _AppReducer(state, action);

}