import { Actions, createEffect, ofType } from "@ngrx/effects";
import { MqttClientService } from "../../services/mqtt-client.service";
import { Injectable } from "@angular/core";
import { EMPTY, Observable, catchError, exhaustMap, filter, from, map, mergeMap, of, switchMap, take, takeUntil, tap } from "rxjs";
import { IMqttMessage, MqttService } from "ngx-mqtt";
import { connectMQTT, connectionClosed, connectionFailure, connectionSuccess, desconnectMQTT, onSubscribeMQTT, onSubscribeStatusClients, stopSubscribeStatusClients, subscribeFailure, subscribeMQTT, subscribeStatusClients, subscribeSuccess } from "../actions/Mqtt.Action";
import { RobotDto } from "../models/Robot/RobotDto.model";
import {  updateRobotStatusConnectionSuccess} from "../actions/Robot.Action";
import { RobotState } from "../states/Robot.state";
import { Store } from "@ngrx/store";
import { ShowAlert } from "../actions/Global.Action";
import { ReponseStatus } from "../models/Global/ReponseStatus.enum";
import { StatusClientMQTT } from "../models/Global/StatusClientMQTT.model";

@Injectable()
export class MqttEffects {
    constructor(private actions$: Actions, private mqttClientService: MqttClientService,   private store: Store<RobotState>  ) { }

    _connectMQTTEffect$ = createEffect(() => this.actions$.pipe(
      ofType(connectMQTT),
      switchMap(({ options }) => {
        return this.mqttClientService.connect(options).pipe(
          map(() => connectionSuccess()),
          catchError(error => of(connectionFailure({ error })))
        );
      })
    ));
    _desconnectMQTTEffect$ = createEffect(() => this.actions$.pipe(
      ofType(desconnectMQTT),
      switchMap(() => {
        return this.mqttClientService.disconnect().pipe(
          map(() => connectionClosed()),
          catchError(error => of(connectionFailure({ error })))
        );
      })
    ));


    _subscribeStatusClients = createEffect(() =>
      this.actions$.pipe(
          ofType(subscribeStatusClients),
          switchMap(action =>
              { 
              if (!action.subscribe) { return EMPTY;}
               return   this.mqttClientService.subscribe(action.subscribe)!.pipe(
                  // map(message => {
                  //     const statusClient : StatusClientMQTT = JSON.parse(message.payload.toString());
                  //     return onSubscribeStatusClients({ statusClient: statusClient });
                  // }),
                  mergeMap(message => {
                    const statusClient: StatusClientMQTT = JSON.parse(message.payload.toString());
                    return [
                      onSubscribeStatusClients({ statusClient }),
                      updateRobotStatusConnectionSuccess({ client: statusClient })
                    ];
                  }),
                  catchError((_error) =>
                      of(
                          connectionFailure({ error: 'Subscribe Robots failed' }),
                          ShowAlert({
                              title: "Error",
                              datestamp: new Date(),
                              status: ReponseStatus.ERROR,
                              message: _error
                          })
                      )
                  ),takeUntil(this.actions$.pipe(ofType(stopSubscribeStatusClients))) 
              )
               }
           )
      )
  ); 



// subscribeToMQTTTopic = createEffect(() =>
// this.actions$.pipe(
//     ofType(subscribeMQTT),
//     switchMap(action =>   
//        this.mqttClientService.subscribe(action.subscribe)!.pipe(
//         map(message => {
//           const updateRobot = JSON.parse(message.payload.toString());
//           return refreshRobotsuccess({ robotinput : updateRobot });
//         }),
//             catchError((error) => 
//             {
//               return of(connectionFailure({ error: 'Subscribe failed' }));
//             }
//             )
//         )
//      )
// )
// );
 


    /*onConnectEffect$ = createEffect(() => this.mqttClientService.onConnect().pipe(
      tap(() => console.log('MQTT --> Connection succeeded!')), map(() => connectionSuccess())
    ), { dispatch: true });
  
    onErrorEffect$ = createEffect(() => this.mqttClientService.onError().pipe(
      tap(error => console.log('MQTT --> Connection failed', error))
    ), { dispatch: false });
  
    onCloseEffect$ = createEffect(() => this.mqttClientService.onClose().pipe(
      tap(() => console.log('MQTT --> Connection closed.')),
      map(() => connectionClosed())
    ), { dispatch: true });*/
    // onMessageEffect$ = createEffect(() => this.mqttClientService.onMessage().pipe(
    //   map(packet => messageReceived({ payload: packet.payload.toString(), topic: packet.topic }))
    // ));
  //   connect$ = createEffect(() =>
  //   this.actions$.pipe(
  //     ofType(connectMQTT),
  //     switchMap((action) =>
  //       from(this.mqttClientService.connect(action.options)).pipe(
  //         tap(connected => {
  //           if (connected) {
  //             console.log("AAAAAAAAAAAAAAAAAAAAAAAAAA");
  //           } else {
  //             console.log("BBBBBBBBBBBBBBBBBBBBBBBBBB");
  //           }
  //         }),
  //         map((connected) => {
  //           if (connected) {
  //             return connectionSuccess();
  //           } else {
  //             return connectionFailure({ error: 'Failed to connect' });
  //           }
  //         }),
  //         catchError((error) => of(connectionFailure({ error })))
  //       )
  //     )
  //   )
  // );
  // subscribe$ = createEffect(() =>
  //   this.actions$.pipe(
  //     ofType(subscribeMQTT),
  //     switchMap((action) =>
  //       this.mqttClientService.subscribe(action.subscribe).pipe(
  //         map((message: IMqttMessage) => {
  //           // Dispatch subscribeSuccess action
  //           return subscribeSuccess();
  //         }),
  //         tap(() => {
            
  //           console.log('Subscription to MQTT successful.');
           
  //         }),
  //         catchError((error) => of(subscribeFailure({ error })))
  //       )
  //     )
  //   )
  // );
}