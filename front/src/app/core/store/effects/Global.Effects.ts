import { Injectable } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { pipe, exhaustMap, map, switchMap, of, Observable, catchError } from "rxjs"
import { EmptyAction, ShowAlert } from "../actions/Global.Action";
import { MsgResponseStatus } from "../models/Global/MsgResponseStatus.model";
import { ReponseStatus } from "../models/Global/ReponseStatus.enum";

@Injectable()
export class GlobalEffects {

    constructor(private action$: Actions, private _snackbar: MatSnackBar) {

    }
    // _RangeDate = createEffect(() =>
    //     this.action$.pipe(
    //         ofType(searchInputRangeDate),
    //         exhaustMap((action) => { 
    //             const limit = action.rangeDate.end.getTime() - action.rangeDate.start.getTime()
    //             const  obsev  =  new Observable(observer => {
    //                 if (action.rangeDate.limit >= limit) 
    //                    { observer.next({title: "Msg",datestamp: new Date(),status: ReponseStatus.SUCCESSFUL,message: "AAAAAAAAA"}); }
    //                 else { observer.error({title: "Error",datestamp: new Date(),status: ReponseStatus.ERROR,message: "BBBBBBBBBBB"});} 
    //                 observer.complete();
    //             }); 
    //              return obsev
    //             .pipe(
    //                 switchMap(res => of( )),
    //                catchError((_error) => of(   ShowAlert(_error) )   ) 
    //             )
    //         })
    //     )
    // );

    // _RangeDate = createEffect(() =>
    //     this.action$.pipe(
    //         ofType(searchInputRangeDate),
    //         exhaustMap((action) => { 
    //             let msg : MsgResponseStatus ; 
    //             const limit = action.rangeDate.end.getTime() - action.rangeDate.start.getTime()
    //             console.log("effect" , limit );
    //             console.log("state " , action.rangeDate.limit ); 
    //             if (action.rangeDate.limit >= limit) 
    //                 { msg = {title: "Error",datestamp: new Date(),status: ReponseStatus.SUCCESSFUL,message: "AAAAAAAAA"}; }
    //             else { msg = {title: "Error",datestamp: new Date(),status: ReponseStatus.ERROR,message: "BBBBBBBBBBB"};} 
    //              return  this. ShowsnackbarAlert(msg).afterDismissed()
    //             .pipe(
    //                 //switchMap(res => of(  EmptyAction()))

    //                 map(() => {
    //                      return EmptyAction();
    //                 })
    //             );


    //         })
    //     )
    // );



    _ShowAlert = createEffect(() =>
        this.action$.pipe(
            ofType(ShowAlert),
            exhaustMap(action => {
                return this.ShowsnackbarAlert(action)
                    .afterDismissed()
                    .pipe(
                        map(() => {
                            return EmptyAction();
                        })
                    )
            })
        )
    );

    ShowsnackbarAlert( msg :  MsgResponseStatus) {
        const _class =  ( msg.status == ReponseStatus.SUCCESSFUL ? 'green-snackbar' : msg.status == ReponseStatus.UNSUCCESSFUL ? 'yellow-snackbar' : 'red-snackbar');
        return this._snackbar.open(msg.title +" : "+ msg.message + " at " + msg.datestamp.toLocaleString(), 'OK', {
            verticalPosition: 'bottom',
            horizontalPosition: 'end',
            panelClass: [_class],
            duration: 5000
        })
    }

}
 