import { Injectable } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { pipe, exhaustMap, map } from "rxjs"
import { EmptyAction, ShowAlert } from "../actions/Global.Action";
import { MsgResponseStatus } from "../models/Global/MsgResponseStatus.model";
import { ReponseStatus } from "../models/Global/ReponseStatus.enum";

@Injectable()
export class GlobalEffects {

    constructor(private action$: Actions, private _snackbar: MatSnackBar) {

    }

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
 