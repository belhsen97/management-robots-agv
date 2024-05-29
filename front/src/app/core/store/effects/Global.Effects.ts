import { Injectable } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { pipe, exhaustMap, map, switchMap, of, Observable, catchError } from "rxjs"
import { EmptyAction, ShowAlert, searchInputRangeDate, searchInputRangeDateSuccess } from "../actions/Global.Action";
import { MsgResponseStatus } from "../models/Global/MsgResponseStatus.model";
import { ReponseStatus } from "../models/Global/ReponseStatus.enum";
import { RangeDate } from "../states/Global.state";


@Injectable()
export class GlobalEffects {

    constructor(private action$: Actions, private _snackbar: MatSnackBar ,) {

    }
    private formatDuration(ms: number): string {
        const seconds = Math.floor((ms / 1000) % 60);
        const minutes = Math.floor((ms / (1000 * 60)) % 60);
        const hours = Math.floor((ms / (1000 * 60 * 60)) % 24);
        const days = Math.floor(ms / (1000 * 60 * 60 * 24));
      
        const daysString = days > 0 ? `${days}d ` : '';
        const hoursString = hours > 0 ? `${hours}h ` : '';
        const minutesString = minutes > 0 ? `${minutes}m ` : '';
        const secondsString = `${seconds}s`;
      
        return daysString + hoursString + minutesString + secondsString;
      }
    _RangeDate = createEffect(() =>
        this.action$.pipe(
            ofType(searchInputRangeDate),
            exhaustMap((action) => { 
             
                const  obsev  =  new Observable(observer => {
                    //console.log(action.rangeDate);
                    if (action.rangeDate.end == null) 
                        { observer.next(action.rangeDate);observer.complete();}
                    if (action.rangeDate.start == null && action.rangeDate.end == null ) 
                        {    observer.error({title: "Error",datestamp: new Date(),status: ReponseStatus.ERROR,message: "you must insert min one date" }); observer.complete();}
                    const limit = action.rangeDate.end.getTime() - action.rangeDate.start.getTime();
                    if (action.rangeDate.limit >= limit){ observer.next(action.rangeDate);        observer.complete();}
                    else {   observer.error({title: "Error",datestamp: new Date(),status: ReponseStatus.ERROR,message: "range date under then "+ this.formatDuration(action.rangeDate.limit) +" " });        observer.complete(); } 
            
                }); 
                 return obsev
                .pipe(map(res =>    searchInputRangeDateSuccess({rangeDate : res as RangeDate})   ),
                   catchError((_error) => of(   ShowAlert(_error) )   ) 
                )
            })
        )
    );

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
        console.log(msg);
        const _class =  ( msg.status == ReponseStatus.SUCCESSFUL ? 'green-snackbar' : msg.status == ReponseStatus.UNSUCCESSFUL ? 'yellow-snackbar' : 'red-snackbar');
        return this._snackbar.open(msg.title +" : "+ msg.message + " at " + msg.datestamp.toLocaleString(), 'OK', {
            verticalPosition: 'bottom',
            horizontalPosition: 'end',
            panelClass: [_class],
            duration: 5000
        })
    }

}
 