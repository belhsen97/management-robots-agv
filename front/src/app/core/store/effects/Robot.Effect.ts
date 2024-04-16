import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { MatSnackBar } from "@angular/material/snack-bar";
import { EMPTY, Observable, Subscription, catchError, concatMap, exhaustMap, map, mergeMap, of, switchMap, takeUntil, tap } from "rxjs"
import { RobotService } from "../../services/robot.service";
import { addRobot, addRobotsuccess, deleteRobot, deleteRobotsuccess, loadRobots, loadRobotfail, loadRobotsuccess, refreshPannelRobot, updateRobot, updateRobotsuccess, loadDataRobotChartSuccess, loadDataRobotChartbyName, refreshRobotssuccess, refreshRobots, stopRefreshRobots, refreshRobot, refreshRobotsuccess, stopRefreshRobot, loadDataRobotChartByNameAndUnixDatetime } from "../actions/Robot.Action";
import { ShowAlert } from "../actions/Global.Action";
import { ReponseStatus } from "../models/Global/ReponseStatus.enum";
import { RobotDto } from "../models/Robot/RobotDto.model";
import { RobotDataChart } from "../models/Robot/RobotDataChart.model";
import { MqttClientService } from "../../services/mqtt-client.service";
import { connectionFailure } from "../actions/Mqtt.Action";
import { IMqttMessage } from "ngx-mqtt";


@Injectable()
export class RobotEffects {
    constructor(private action$: Actions, private service: RobotService, private mqttClientService: MqttClientService, private _snackbar: MatSnackBar) { }

    _loadRobot = createEffect(() => this.action$
        .pipe(
            ofType(loadRobots),
            exhaustMap((action) => {
                return this.service.getAll().pipe(
                    // map((response) => {return loadRobotsuccess({ listRobots: response.body as RobotDto[]  });}),
                    concatMap((response) => [
                        loadRobotsuccess({ listRobots: response.body as RobotDto[] }),
                        refreshPannelRobot()]
                    ),
                    catchError((_error) =>
                        of(
                            loadRobotfail({ errorMessage: _error }),
                            ShowAlert({
                                title: "Error",
                                datestamp: new Date(),
                                status: ReponseStatus.ERROR,
                                message: _error
                            })
                        )
                    )
                );
            })
        )
    );

    _loadDataRobotByName = createEffect(() => this.action$
        .pipe(
            ofType(loadDataRobotChartbyName),
            exhaustMap((action) => {
                return this.service.getAllDataByName(action.name).pipe(
                    map((response) => { return loadDataRobotChartSuccess({ robotDataChart: response.body as RobotDataChart }); }),
                    catchError((_error) =>
                        of(
                            loadRobotfail({ errorMessage: _error }),
                            ShowAlert({
                                title: "Error",
                                datestamp: new Date(),
                                status: ReponseStatus.ERROR,
                                message: _error
                            })
                        )
                    )
                );
            })
        )
    );
    _loadDataRobotByNameAndUnixDatetime = createEffect(() => this.action$
        .pipe(
            ofType(loadDataRobotChartByNameAndUnixDatetime),
            exhaustMap((action) => {
                return this.service.geAllDataByNameAndUnixDatetime(action.name, {start:action.start,end:action.end}).pipe(
                    map((response) => { return loadDataRobotChartSuccess({ robotDataChart: response.body as RobotDataChart }); }),
                    catchError((_error) =>
                        of(
                            loadRobotfail({ errorMessage: _error }),
                            ShowAlert({
                                title: "Error",
                                datestamp: new Date(),
                                status: ReponseStatus.ERROR,
                                message: _error
                            })
                        )
                    )
                );
            })
        )
    );


    _AddRobot = createEffect(() =>
        this.action$.pipe(
            ofType(addRobot),
            switchMap(action =>
                this.service.insert(action.robotinput).pipe(
                    switchMap(response => of(
                        addRobotsuccess({ robotinput: response.body as RobotDto }),
                        ShowAlert({ title: "Message", datestamp: new Date(), status: ReponseStatus.SUCCESSFUL, message: "success add new robot" })
                    )),
                    catchError((error) => {
                        let errorMsg;
                        if (error.status === 406 || error.status === 403) {
                            errorMsg = error.error;
                        } else {
                            errorMsg = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message };
                        }
                        return of(loadRobotfail({ errorMessage: errorMsg }), ShowAlert(errorMsg));
                    }
                    )
                )
            )
        )
    );

    _UpdateRobot = createEffect(() =>
        this.action$.pipe(
            ofType(updateRobot),
            switchMap(action =>
                this.service.update(action.id, action.robotinput).pipe(
                    switchMap(response => of(
                        updateRobotsuccess({ robotinput: response.body as RobotDto }),
                        ShowAlert({ title: "Message", datestamp: new Date(), status: ReponseStatus.SUCCESSFUL, message: "success update robot" }),
                        refreshPannelRobot()
                    )),
                    catchError((error) => {
                        let errorMsg;
                        if (error.status === 406 || error.status === 403) {
                            errorMsg = error.error;
                        } else {
                            errorMsg = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message };
                        }
                        return of(loadRobotfail({ errorMessage: errorMsg }), ShowAlert(errorMsg));
                    }
                    )
                )
            )
        )
    );

    _DeleteRobot = createEffect(() =>
        this.action$.pipe(
            ofType(deleteRobot),
            switchMap(action => {
                return this.service.delete(action.id).pipe(
                    switchMap((response) => of(
                        deleteRobotsuccess({ id: action.id }),
                        ShowAlert(response.body),
                        refreshPannelRobot()
                    )),
                    catchError((error) => {
                        let errorMsg;
                        if (error.status === 406 || error.status === 403) {
                            errorMsg = error.error;
                        } else {
                            errorMsg = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message };
                        }
                        return of(loadRobotfail({ errorMessage: errorMsg }), ShowAlert(errorMsg));
                    }
                    )
                )
            })
        )
    );




    refreshAllRobots = createEffect(() =>
        this.action$.pipe(
            ofType(refreshRobots),
            switchMap(action =>
                { 
                if (!action.subscribe) { return EMPTY;}
                 return   this.mqttClientService.subscribe(action.subscribe)!.pipe(
                    map(message => {
                        const updateRobot = JSON.parse(message.payload.toString());
                        return refreshRobotssuccess({ robotinput: updateRobot });
                    }),
                    catchError((_error) =>
                        of(
                            connectionFailure({ error: 'Subscribe Robots failed' }),
                            loadRobotfail({ errorMessage: _error }),
                            ShowAlert({
                                title: "Error",
                                datestamp: new Date(),
                                status: ReponseStatus.ERROR,
                                message: _error
                            })
                        )
                    ),takeUntil(this.action$.pipe(ofType(stopRefreshRobots))) 
                )
                 }
             )
        )
    );


    refreshOneRobot = createEffect(() =>
    this.action$.pipe(
        ofType(refreshRobot),
        switchMap(action =>
            { 
            if (!action.subscribe) { return EMPTY;}
             return   this.mqttClientService.subscribe(action.subscribe)!.pipe(
                map(message => {
                    const updateRobot = JSON.parse(message.payload.toString());
                    return refreshRobotsuccess({ robotinput: updateRobot });
                }),
                catchError((_error) =>
                    of(
                        connectionFailure({ error: 'Subscribe Robot failed' }),
                        loadRobotfail({ errorMessage: _error }),
                        ShowAlert({
                            title: "Error",
                            datestamp: new Date(),
                            status: ReponseStatus.ERROR,
                            message: _error
                        })
                    )
                ),takeUntil(this.action$.pipe(ofType(stopRefreshRobot))) 
            )
             }
         )
    )
);
}