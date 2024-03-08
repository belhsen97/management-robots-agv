import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs"; 
import { UserService } from "./user.service.ts.service";


@Injectable()
export class ActivateRouteService implements CanActivate,CanActivateChild  {
    constructor( private userService : UserService) { }


    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        if (this.userService.isAuthenticated(route,state)) {
         return true;
        } else {
         this.userService.goToComponent("sign-in");
         return false;
        }

    }
    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        return this.canActivate ( childRoute , state );
    }
}