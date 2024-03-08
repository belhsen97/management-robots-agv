import { Params, RouterStateSnapshot } from "@angular/router";
import {RouterStateSerializer} from "@ngrx/router-store"


//  configuration from   https://ngrx.io/guide/router-store/configuration
export interface RouterStateModel{
    url:string,
    params:Params,
    queryParams:Params
}

export class DashbordRouterSerializer implements RouterStateSerializer<RouterStateModel>{
    serialize(routerState: RouterStateSnapshot): RouterStateModel {
        let route=routerState.root;
        while (route.firstChild) {
            route = route.firstChild;
          }
       
          const {
            url,
            root: { queryParams },
          } = routerState;
          const { params } = route;
       
          // Only return an object including the URL, params and query params
          // instead of the entire snapshot
          return { url, params, queryParams };
    }

}