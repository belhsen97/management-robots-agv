import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Store } from '@ngrx/store';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { GlobalState } from '../store/states/Global.state';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor( private storeGlobal: Store<GlobalState>) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let msgResponseStatus: any;
        if ((error.status === 406 )||(error.status === 403 )) {
         msgResponseStatus = error.error;
        } else {
          msgResponseStatus = {
            title: "Error",
            datestamp: new Date(),
            status: ReponseStatus.ERROR,
            message: error.message
          };
        } 
        console.log(  error.error);
        this.storeGlobal.dispatch(ShowAlert(msgResponseStatus));
      //  console.error('Error status:', error.status, 'Error message:', error.message);

        return throwError(() => error);
      })
    );
  }
}


// register user  if (error.status === 406 ||error.status === 404|| error.status === 500 ) {  this.userService.msgResponseStatus  = error.error; }
//will replace 
// this.tagService.getAll().subscribe(
//   (response) => { 
//     tagState.listTags = response.body; 
//     this.dataSource.data =    tagState.listTags ;
//   }
//   ,(error:HttpErrorResponse) => {
//     this.tagService.msgResponseStatus  =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
//     this.store.dispatch( ShowAlert(this.tagService.msgResponseStatus ) ); 
//     //this.workstationService.goToComponent("/sign-in");
//   }) ;