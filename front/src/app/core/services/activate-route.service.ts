import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { UserService } from "./user.service.ts.service";
import { Role } from "../store/models/User/Role.enum";


@Injectable()
export class ActivateRouteService implements CanActivate, CanActivateChild {
    private readonly listPathPermision = [
        { role: Role.ADMIN, path: '/edit-user/**' },
        { role: Role.MAINTENANCE, path: '/edit-user/**' },
        { role: Role.OPERATOR, path: '/edit-user/**' },
        { role: Role.MAINTENANCE, path: '/list-workstations/**' },
        { role: Role.MAINTENANCE, path: '/list-workstations/**' }];




    constructor(private userService: UserService) { }


    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        if (this.isAuthenticated(route, state)) {
            return true;
        } else {
            this.userService.goToComponent("sign-in");
            return false;
        }

    }
    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        return this.canActivate(childRoute, state);
    }






    private isAuthenticated(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        console.log('state.url: ', state.url);
        const pathFromRoot = route.pathFromRoot.map(route => route.url.join('/')).join('/');
        console.log('Full path: ', pathFromRoot);
        console.log(this.isPathValid(pathFromRoot, this.listPathPermision));
        return true;//????????????????????????????????????????????????????????????????????????????????????
    }
    private isPathValid(pathToCheck: string, listPathPermission: any): boolean {
        for (const pathPermission of listPathPermission) {
            const pathPattern = pathPermission.path.replace(/\*\*/g, '.*');
            const regex = new RegExp(`^${pathPattern}$`);

            if (regex.test(pathToCheck) && this.userService.getUserDto().role == pathPermission.role) {
                return true;
            }
        }
        return false;
    }
}