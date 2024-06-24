import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { MatSnackBar } from "@angular/material/snack-bar";
import { EMPTY, catchError, concatMap, exhaustMap, map, of, switchMap, takeUntil } from "rxjs"
import { RobotService } from "../../services/robot.service";
import { addRobot, addRobotsuccess, deleteRobot, deleteRobotsuccess, loadRobots, loadRobotfail, loadAllRobotsuccess, refreshPannelRobot, updateRobot, updateRobotsuccess, loadDataRobotChartSuccess, loadDataRobotChart, listenerAllRobotsSuccess, startListenerAllRobots, stopListenerAllRobots, startListenerRobot, listenerRobotsuccess, stopListenerRobot, loadRobotByName, loadRobotsuccess, loadRobotDataBandSuccess, loadSettingRobot, loadSettingRobotSuccess, updateSettingRobot, loadDataRobotData, loadListRobotDataBandSuccess, startListenerAllRobotsByProperty, stopListenerAllRobotsByProperty, listenerAllRobotsByPropertySuccess, startListenerRobotByProperty, stopListenerRobotByProperty, listenerRobotByPropertySuccess } from "../actions/Robot.Action";
import { ShowAlert } from "../actions/Global.Action";
import { ReponseStatus } from "../models/Global/ReponseStatus.enum";
import { RobotDto } from "../models/Robot/RobotDto.model";
import { RobotDataChart } from "../models/Robot/RobotDataChart.model";
import { MqttClientService } from "../../services/mqtt-client.service";
import { connectionFailure } from "../actions/Mqtt.Action";
import { RobotDataBand } from "../models/Robot/RobotDataBand.model";
import { RobotSettingDto } from "../models/Robot/RobotSettingDto.model";


@Injectable()
export class RobotEffects {
    constructor(private action$: Actions, private service: RobotService, private mqttClientService: MqttClientService, private _snackbar: MatSnackBar) { }

    _loadRobots = createEffect(() => this.action$
        .pipe(
            ofType(loadRobots),
            exhaustMap((action) => {
                return this.service.getAll().pipe(
                    // map((response) => {return loadRobotsuccess({ listRobots: response.body as RobotDto[]  });}),
                    concatMap((response) => [
                        loadAllRobotsuccess({ listRobots: response.body as RobotDto[] }),
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

    _loadRobotbyName = createEffect(() => this.action$
    .pipe(
        ofType(loadRobotByName),
        exhaustMap((action) => {
            return this.service.getByName(action.name).pipe(
                map((response) => {return loadRobotsuccess({ robotinput: response.body as RobotDto  });}),
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
_loadSettingRobot = createEffect(() => this.action$
.pipe(
    ofType(loadSettingRobot),
    exhaustMap((action) => {
        return this.service.getAllRobotConfig( ).pipe(
            map((response) => {return loadSettingRobotSuccess({ setting: response.body as RobotSettingDto  });}),
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

//  _loadDataChartRobotByName = createEffect(() => this.action$
//         .pipe(
//             ofType(loadDataRobotChartbyName),
//             exhaustMap((action) => {
//                 return this.service.getAllDataChartByName(action.name).pipe(
//                     map((response) => { return loadDataRobotChartSuccess({ robotDataChart: response.body as RobotDataChart }); }),
//                     catchError((_error) =>
//                         of(
//                             loadRobotfail({ errorMessage: _error }),
//                             ShowAlert({
//                                 title: "Error",
//                                 datestamp: new Date(),
//                                 status: ReponseStatus.ERROR,
//                                 message: _error
//                             })
//                         )
//                     )
//                 );
//             })
//         )
//     );

    _loadDataChartRobot = createEffect(() => this.action$
        .pipe(
            ofType(loadDataRobotChart),
            exhaustMap((action) => {
                return this.service.geDataChartRobot( {name : action.name , start:action.start,end:action.end}).pipe(
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









    _loadDataBandRobot = createEffect(() => this.action$
        .pipe(
            ofType(loadDataRobotData),
            exhaustMap((action) => {
                return this.service.geAllDataBand({ name : action.name,start:action.start,end:action.end}).pipe(
                    map((response) => { 
                        if (  response.body instanceof Array ) {  return loadListRobotDataBandSuccess({ list: response.body as RobotDataBand[] }); }
                        return loadRobotDataBandSuccess({ robotDataBand: response.body as RobotDataBand }); 
                    }),
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
    _UpdateSettingRobot = createEffect(() =>
        this.action$.pipe(
            ofType(updateSettingRobot),
            switchMap(action =>
                this.service.updateRobotConfig(action.settinginput).pipe(
                    switchMap(response => of(
                        loadSettingRobotSuccess({ setting: response.body as RobotSettingDto  }),
                        ShowAlert({ title: "Message", datestamp: new Date(), status: ReponseStatus.SUCCESSFUL, message: "success update robot setting" }),
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




    listenerAllRobots = createEffect(() =>
        this.action$.pipe(
            ofType(startListenerAllRobots),
            switchMap(action =>
                { 
                if (!action.subscribe) { return EMPTY;}
                 return   this.mqttClientService.subscribe(action.subscribe)!.pipe(
                    map(message => {
                        const updateRobot = JSON.parse(message.payload.toString());
                        return listenerAllRobotsSuccess({ robotinput: updateRobot });
                    }),
                    catchError((_error) =>
                        of(
                            connectionFailure({ error: 'listener All Robots failed' }),
                            loadRobotfail({ errorMessage: _error }),
                            ShowAlert({
                                title: "Error",
                                datestamp: new Date(),
                                status: ReponseStatus.ERROR,
                                message: _error
                            })
                        )
                    ),takeUntil(this.action$.pipe(ofType(stopListenerAllRobots))) 
                )
                 }
             )
        )
    );
    listenerOneRobot = createEffect(() =>
        this.action$.pipe(
            ofType(startListenerRobot),
            switchMap(action =>
                { 
                if (!action.subscribe) { return EMPTY;}
                 return   this.mqttClientService.subscribe(action.subscribe)!.pipe(
                    map(message => {
                        const updateRobot = JSON.parse(message.payload.toString());
                        return listenerRobotsuccess({ robotinput: updateRobot });
                    }),
                    catchError((_error) =>
                        of(
                            connectionFailure({ error: 'Listener Robot failed' }),
                            loadRobotfail({ errorMessage: _error }),
                            ShowAlert({
                                title: "Error",
                                datestamp: new Date(),
                                status: ReponseStatus.ERROR,
                                message: _error
                            })
                        )
                    ),takeUntil(this.action$.pipe(ofType(stopListenerRobot))) 
                )
                 }
             )
        )
    );
    listenerAllRobotsByProperty = createEffect(() =>
        this.action$.pipe(
            ofType(startListenerAllRobotsByProperty),
            switchMap(action =>
                { 
                if (!action.subscribe) { return EMPTY;}
                 return   this.mqttClientService.subscribe(action.subscribe)!.pipe(
                    map(message => {
                       const property = JSON.parse(message.payload.toString());
                       if (!property.hasOwnProperty("value")) {throw new Error('[Listener AllRobots By Property] cannot found key value of propertie in topic' +message.topic.toString()); }
                        return listenerAllRobotsByPropertySuccess({ topic: message.topic.toString(),value:property.value});
                    }),
                    catchError((_error) =>
                        of(
                            connectionFailure({ error: 'listener All Robots By Property failed' }),
                            loadRobotfail({ errorMessage: _error }),
                            ShowAlert({
                                title: "Error",
                                datestamp: new Date(),
                                status: ReponseStatus.ERROR,
                                message: _error
                            })
                        )
                    ),takeUntil(this.action$.pipe(ofType(stopListenerAllRobotsByProperty))) 
                )
                 }
             )
        )
    );
    listenerOneRobotByProperty = createEffect(() =>
        this.action$.pipe(
            ofType(startListenerRobotByProperty),
            switchMap(action =>
                { 
                if (!action.subscribe) { return EMPTY;}
                 return   this.mqttClientService.subscribe(action.subscribe)!.pipe(
                    map(message => {
                       const property = JSON.parse(message.payload.toString());
                       if (!property.hasOwnProperty("value")) {throw new Error('[Listener Robot By Property] cannot found key value of propertie in topic' +message.topic.toString()); }
                        return listenerRobotByPropertySuccess({ topic: message.topic.toString(),value:property.value});
                    }),
                    catchError((_error) =>
                        of(
                            connectionFailure({ error: 'listener One Robot By Property failed' }),
                            loadRobotfail({ errorMessage: _error }),
                            ShowAlert({
                                title: "Error",
                                datestamp: new Date(),
                                status: ReponseStatus.ERROR,
                                message: _error
                            })
                        )
                    ),takeUntil(this.action$.pipe(ofType(stopListenerRobotByProperty))) 
                )
                 }
             )
        )
    ); 
}