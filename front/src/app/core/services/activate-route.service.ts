import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { UserService } from "./user.service.ts.service";
import { Role } from "../store/models/User/Role.enum";
import { userState } from "../store/states/User.state";
import { UserDto } from "../store/models/User/UserDto.model";
import { HttpErrorResponse } from "@angular/common/http";


@Injectable()
export class ActivateRouteService implements CanActivate, CanActivateChild {

    constructor(private userService: UserService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        return this.isAuthenticate(route, state);
    }
    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        return this.isAuthenticate(childRoute, state);
    } 

    private isAuthenticate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean  | Observable<boolean>  {  
        if (userState.userDto == null) {
            return new Observable<boolean>((observer) => {
                this.userService.getByAuthenticate().subscribe(
                    (response) => {
                        userState.userDto = response.body as UserDto;
                        const isValid = this.isPathValid(state.url, userState.listPathPermision);
                        observer.next(isValid);
                        observer.complete();
                    },
                    (error: HttpErrorResponse) => {
                        this.userService.goToComponent("/sign-in");
                        observer.next(false);
                        observer.complete();
                    }
                );
            });
        } 
           // return this.isPathValid(state.url, userState.listPathPermision);
           if (this.isPathValid(state.url, userState.listPathPermision)) {  return true;  } else {
            this.userService.goToComponent("sign-in");
            return false; }  
    }

    private isPathValid(pathToCheck: string, listPathPermission: any): boolean {  
        for (const pathPermission of listPathPermission) {
            const pathPattern = pathPermission.path.replace(/\*\*/g, '.*');
            const regex = new RegExp(`^${pathPattern}$`); 
            if (regex.test(pathToCheck) && userState.userDto?.role == pathPermission.role) { 
                return true;
            }
        }
        return false;
    }
}  