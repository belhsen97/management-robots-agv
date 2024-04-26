import { Injectable } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { pipe, exhaustMap, map } from "rxjs"
import { EmptyAction, ShowAlert, searchInputRangeDate } from "../actions/Global.Action";
import { MsgResponseStatus } from "../models/Global/MsgResponseStatus.model";
import { ReponseStatus } from "../models/Global/ReponseStatus.enum";

@Injectable()
export class GlobalEffects {

    constructor(private action$: Actions, private _snackbar: MatSnackBar) {

    }

    _RangeDate = createEffect(() =>
        this.action$.pipe(
            ofType(searchInputRangeDate),
            exhaustMap((action) => {
                let func;
                let msg = ""; 
                const limit = action.rangeDate.end.getTime() - action.rangeDate.start.getTime()
                console.log("effect" , limit );
                console.log("state " , action.rangeDate.limit ); 
                if (action.rangeDate.limit >= limit) {
                    func =   this.ShowsnackbarAlert({
                        title: "Error",
                        datestamp: new Date(),
                        status: ReponseStatus.ERROR,
                        message: "AAAAAAAAA"
                    });
                }
                else {
                func =  this.ShowsnackbarAlert({
                    title: "Error",
                    datestamp: new Date(),
                    status: ReponseStatus.ERROR,
                    message: "BBBBBBBBBBB"
                });  } 
                 return   func   .afterDismissed()
                .pipe(
                    map(() => {
                        return EmptyAction();
                    })
                );


            })
        )
    );



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
 