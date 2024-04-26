import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts/highstock';

import dataInit from 'highcharts/modules/data';
import seriesLabelInit from 'highcharts/modules/series-label';
import exportingInit from "highcharts/modules/exporting";
import exportDataInit from "highcharts/modules/export-data";
import accessibilityInit from "highcharts/modules/accessibility";
import noDataToDisplayInit from 'highcharts/modules/no-data-to-display';
import annotationsInit from 'highcharts/modules/annotations';

import { RobotService } from 'src/app/core/services/robot.service';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';
import { Subscription, debounceTime, throttleTime } from 'rxjs';
import { Store } from '@ngrx/store';
import { getRouterName } from 'src/app/core/store/selectors/Router.Seletor';
import { MqttClientService } from 'src/app/core/services/mqtt-client.service';
import { Subscribe } from 'src/app/core/store/models/Mqtt/Subscribe.model';
import { getDataRobotChart, getRobot } from 'src/app/core/store/selectors/Robot.Selector';
import { loadDataRobotChartByNameAndUnixDatetime, loadDataRobotChartbyName, refreshRobot, stopRefreshRobot } from 'src/app/core/store/actions/Robot.Action';

import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { RobotDataChart } from 'src/app/core/store/models/Robot/RobotDataChart.model';
import { StatusRobot } from 'src/app/core/store/models/Robot/StatusRobot.enum';
import { OperationStatus } from 'src/app/core/store/models/Robot/OperationStatus.enum';
import { Connection } from 'src/app/core/store/models/Robot/Connection.enum';
import { ModeRobot } from 'src/app/core/store/models/Robot/ModeRobot.enum';

dataInit(Highcharts);
seriesLabelInit(Highcharts);
exportingInit(Highcharts);
exportDataInit(Highcharts);
accessibilityInit(Highcharts);
noDataToDisplayInit(Highcharts);
annotationsInit(Highcharts);

@Component({
    selector: 'app-stock-chart',
    templateUrl: './stock-chart.component.html',
    styleUrls: ['./stock-chart.component.css']
})
export class StockChartComponent implements OnInit, AfterViewInit, OnDestroy {

    private getRouterNameSub: Subscription | undefined;
    private getDataRobotChartSub: Subscription | undefined;
    private getRobotSub : Subscription | undefined;
    private curSubscription: Subscription | undefined;
    private subTopicNameRobot: Subscribe = { topic: "topic/robot/data/", qos: 0 };
    private nameRobot: String = "";
    private chart: any;
    private connectionState : Connection = Connection.DISCONNECTED;
    private modeState : ModeRobot = ModeRobot.MANUAL;


    constructor(private storeRouter: Store, 
        private robotService: RobotService,
        private mqttClientService: MqttClientService,
        private storeRobot: Store<RobotState>,
    ) { }

    ngOnInit(): void {
        this.getRouterNameSub = this.storeRouter.select(getRouterName).subscribe(item => {
            if (item === null || item === undefined) { return; }
            this.nameRobot = item;
        });
        if (this.getRouterNameSub) { this.getRouterNameSub.unsubscribe(); }
        if (this.nameRobot == "") {this.robotService.goToComponent("dashboard/table");}

        this.subTopicNameRobot.topic += this.nameRobot;
   
        this.chart = Highcharts.stockChart('container-stock-chart-robot', this.chartOptions);

        this.chart.showLoading("Loading data from server...");
        this.getDataRobotChartSub = this.storeRobot.select(getDataRobotChart).subscribe(item => {
            if (item.speed.length !== 0) { this.chart.series[0].setData(item.speed); } //if (this.state) {this.chart.navigator.series[0].setData(item.speed);}
   

            if (item.battery.length !== 0) { this.chart.series[1].setData(item.battery); }
            if (item.statusRobot.length !== 0) { this.chart.series[2].setData(item.statusRobot); }
            if (item.operationStatus.length !== 0) { this.chart.series[3].setData(item.operationStatus); }
            item.connectionPlotBand.forEach((data) => {
                this.chart.xAxis[0].addPlotBand({
                    from: data.from,
                    to: data.to,
                    color: "#F7CAC9",
                });
            });
            item.connectionPlotLine.forEach((data) => {
                this.chart.xAxis[0].addPlotLine({
                    color: '#000000', width: 1, value: data.value,
                    label: { text: data.text, rotation: (data.text == "CONNECTED" ? 90 : -90), x: (data.text == "CONNECTED" ? 5 : -5), y: (data.text == "CONNECTED" ? 5 : 110) }
                });
            });
            item.modePlotLine.forEach((data) => {
                this.chart.xAxis[0].addPlotLine({
                    color: '#000000', width: 1, value: data.value,
                    label: {  verticalAlign: 'bottom',text: data.text, rotation: (data.text == "AUTO" ? 90 : -90), x: (data.text == "AUTO" ? 5 : -5) }
                });
            });
            //   robotState.robotDataChart!.modePlotBand.forEach((data) => {
            //     this.chart.xAxis[0].addPlotBand({
            //         from: data.from,
            //         to: data.to,
            //         color: "#EDD59E"
            //       });
            //   });
         
            this.chart.hideLoading();
        }
        );
        this.getRobotSub = this.storeRobot.select(getRobot).pipe(throttleTime(500)).subscribe(r => {
          const x = (new Date()).getTime();
           let y = r.speed;
           this.chart.series[0].addPoint([x,y], true, false);   //addPoint(options [, redraw] [, shift] [, animation] [, withEvent])
           y = r.levelBattery;
           this.chart.series[1].addPoint([x,y], true, false);
           y = (r.statusRobot == StatusRobot.RUNNING ?  2 : (r.statusRobot == StatusRobot.WAITING ? 1 : 0));
           this.chart.series[2].addPoint([x,y], true, false);
           y = (r.operationStatus == OperationStatus.PAUSE ?  2 : (r.operationStatus == OperationStatus.EMS ? 1 : 0));
           this.chart.series[3].addPoint([x,y], true, false);
           if ( this.connectionState != r.connection ){
            this.chart.xAxis[0].addPlotLine({
                color: '#000000', width: 1, value: x,
                label: { text:  r.connection , rotation: ( r.connection  == "CONNECTED" ? 90 : -90), x: ( r.connection   == "CONNECTED" ? 5 : -5), y: ( r.connection  == "CONNECTED" ? 5 : 110) }
            });
            this.connectionState = r.connection;
           }
           //if ( this.connectionState == Connection.CONNECTED ){ }
           //if ( this.connectionState == Connection.DISCONNECTED ){ }
           if ( this.modeState != r.modeRobot ){
            this.chart.xAxis[0].addPlotLine({
                color: '#000000', width: 1, value: x,
                label: {  verticalAlign: 'bottom', text:  r.modeRobot, rotation: ( r.modeRobot == "AUTO" ? 90 : -90), x: ( r.modeRobot == "AUTO" ? 5 : -5)}
            });
            this.modeState = r.modeRobot;
           }
        }
        );
       this.storeRobot.dispatch(loadDataRobotChartbyName({ name: this.nameRobot }));
 


    }


  
    ngAfterViewInit() {
        this.storeRobot.dispatch(refreshRobot({ subscribe: this.subTopicNameRobot }));
    }

    ngOnDestroy(): void {
        //this.mqttClientService.closeSubscribe(this.curSubscription);
        this.storeRobot.dispatch(stopRefreshRobot());
        this.chart.destroy();
        if (this.getRouterNameSub) { this.getRouterNameSub.unsubscribe(); }
        if (this.getDataRobotChartSub) { this.getDataRobotChartSub.unsubscribe(); }
        if (this.getRobotSub) {this.getRobotSub.unsubscribe();}
    } 
 
    isExtremesSetDueToNavigatorMove: boolean = false;

    chartOptions: any = {
        title: {
            useHTML: true,
            text: 'Robot AGV Statistics',
            align: 'center',
            margin: 20,
            x: 0, y: 20,
            style: {
                color: '#000000', fontWeight: 'normal',
                fontSize: '24px', 'letter-spacing': '0.2em'
                , 'font-family': 'Cambria'
            }
        },
        subtitle: {
            text: 'properities of robot in relative time',
            align: 'center',
            x: 0, y: 45,
            style: {
                color: '#000000', fontWeight: 'normal',
                fontSize: '18px', 'letter-spacing': '0.1em'
                , 'font-family': 'Cambria'
            }
        },
        plotOptions: {
            series: {
                animation: {
                    duration: 4000
                }
            }
        },
        navigator: {
            enabled: true, height: 50, margin: 10,
            maskFill: 'rgba(180, 198, 220, 0.75)',
            maskInside: true,
            //adaptToUpdatedData: false,
            // series: [{
            //     data: robotState.robotData!.speed,
            //}]
        },


        rangeSelector: {
            floating: false,
            inputEnabled: false,
            inputPosition: {
                align: 'left',
                x: 0,
                y: 0
            },
            buttonPosition: {
                align: 'right',
                x: 0,
                y: 0
            },

            buttons: [
                {
                    type: 'minute',
                    count: 1,
                    text: '1m'
                }, {
                    type: 'minute',
                    count: 5,
                    text: '5m'
                }, {
                    type: 'minute',
                    count: 30,
                    text: '30m'
                }, {
                    type: 'hour',
                    count: 1,
                    text: '1h'
                }, {
                    type: 'hour',
                    count: 12,
                    text: '12h'
                }, {
                    type: 'day',
                    count: 1,
                    text: '1d'
                }

                // {
                //     type: 'month',
                //     count: 1,
                //     text: '1m',
                //     events: {
                //         click: function() {
                //             alert('Clicked button');
                //         }
                //     }
                // }, {
                //     type: 'month',
                //     count: 3,
                //     text: '3m'
                // }, {
                //     type: 'month',
                //     count: 6,
                //     text: '6m'
                // }, {
                //     type: 'ytd',
                //     text: 'YTD'
                // }, {
                //     type: 'year',
                //     count: 1,
                //     text: '1y'
                // }
            ], buttonTheme: { width: 30 },
            selected: 1, allButtonsEnabled: true
        },
        yAxis: [{
            title: { enabled: true, text: "Speed", style: { color: '#000000', fontSize: '14px' } },
            labels: { format: '{text}m/s', align: 'right', x: -2, style: { color: '#000000', fontSize: '14px' } },
            gridLineWidth: 0,  // lineColor: '#FF0000',lineWidth: 1,
            top: '0%', height: '75%', //,  height: '50%', 
            //alternateGridColor: '#FDFFD5',
            crosshair: true,
            plotLines: [{
                value: robotState.settingRobot!.speed.max,
                color: 'red',
                dashStyle: 'shortDash',
                width: 2,
                zIndex: 1000,
                label: { align: 'left', text: 'max speed', x: 30, style: { color: '#DD4124', fontWeight: 'bold', fontSize: '14px' } }
            },
            {
                value: robotState.settingRobot!.speed.min,
                color: 'red',
                dashStyle: 'shortDash',
                width: 2,
                zIndex: 1000,
                label: { text: 'min speed', align: 'left', x: 30, style: { color: '#DD4124', fontWeight: 'bold', fontSize: '14px' } }
            }
            ],
        }
            , { // right y axis
            // linkedTo: 1,
            min: 0, max: 100,
            title: { enabled: true, text: "Battery Level", style: { color: '#000000', fontSize: '14px' } },
            labels: { format: '{value:.,0f}%', align: 'left', x: -3, style: { color: '#000000', fontSize: '14px' } },
            gridLineWidth: 0,
            endOnTick: false, // over limit y axis
            opposite: false,
            top: '0%', height: '75%',//top: '50%',height: '50%',
            offset: 0,
            crosshair: true
        },
        {
            title: { enabled: true, text: 'Status', style: { color: '#000000', fontSize: '14px' } },
            labels: { align: 'right', x: 0, style: { color: '#000000', fontSize: '10px' } },
            top: '75%',
            height: '25%',
            opposite: true,
            offset: 0,
            categories: ['INACTIVE', 'WAITING', 'RUNNING'],
            crosshair: true
        },
        {
            title: { enabled: true, text: 'Operation Status', style: { color: '#000000', fontSize: '14px' } },
            labels: { align: 'left', x: 0, style: { color: '#000000', fontSize: '10px' } },
            top: '75%',
            height: '25%',
            opposite: false,
            offset: 0,
            categories: ['NORMAL', 'EMS', 'PAUSE'],
            crosshair: true,//Configure a crosshair that follows either the mouse pointer or the hovered point.
            //reversed: true,
        }
        ],

        xAxis: {
            events: {
               afterSetExtremes:  (e: any) => {
                const { chart } = e.target;
                this.chart = chart;
                console.log("afterSetExtremes");
                if (this.isExtremesSetDueToNavigatorMove && e.max !== e.dataMax) {
                  // chart.showLoading("Loading data from server...");
                    //console.log(e.min); console.log(e.max);
                    const min = Math.floor(e.min);
                    const max = Math.floor(e.max);
                //  this.storeRobot.dispatch(loadDataRobotChartByNameAndUnixDatetime({ name:  this.nameRobot, start:min ,end:max }));
                }
                this.isExtremesSetDueToNavigatorMove = false;
            },
                setExtremes: () => {  this.isExtremesSetDueToNavigatorMove = true ;  }
            },
            //crosshair: true,
            //range: 12*60*60*1000,
            //minRange: 60*60*1000,
            //maxRange: 24*60*60*1000,
            plotBands: [
                //   {
                //     from: 1647523800000,
                //     to: 1647869400000,
                //     color: '#FED9C9',
                //     zIndex: 2,
                //     label: {
                //         text: 'Disconnected',
                //         align: 'center',
                //         x: 0
                //     }
                // },
                // {
                //     from: 1647437400000,
                //     to: 1647523800000,
                //     color: '#FFEFD8',
                //     zIndex: 1,
                //     label: {
                //         text: 'Manual',
                //         align: 'center',
                //         x: 0,y:40
                //     }
                // },
                // {
                //     from: 1647869400000,
                //     to: 1647955800000,
                //     color: '#8FE9C6',
                //     label: {
                //         text: 'connected',
                //         align: 'center',
                //         x: 0
                //     }
                // }
            ]
            , plotLines: [{
                value: 1647523800000,
                color: '#000000',
                width: 1, label: { text: 'Plot line', rotation: -90, x: -5, y: 50 },


                //         //                     align: 'center',
                //  x: 0,y:-10, rotation: -90


            }, {
                value: 1647869400000,
                color: '#000000',
                width: 1
            }],
        },
        tooltip: {
            outside: true,
            enabled: true,
            split: true,//grouping
            shadow: true,
            //snap: 20,
            borderWidth: 1,
            borderRadius: 5,
            distance: 10,
            backgroundColor: { linearGradient: [0, 0, 0, 60], stops: [[0, '#FFFFFF'], [1, '#E0E0E0']] },
        },
        lang: {
            noData: 'Data Empty'
        },
        noData: {
            style: {
                fontWeight: 'bold',
                fontSize: '15px',
                color: '#303030'
            }
        },
        legend: {
            enabled: true,
            floating: false,
            borderWidth: 1,
            borderRadius: 5,
            layout: 'horizontal',
            align: 'center',
            verticalAlign: 'top',
            itemDistance: 10,
            margin: 50,
            itemHiddenStyle: {
                color: 'black'
            },
            x: 0,
            y: -40
        },
        series: [{
            yAxis: 0,
            type: 'spline',
            name: 'Speed',
            color: '#5B5EA6',
            data: [[]],
            dataGrouping: { enabled: false },
            tooltip: { valueDecimals: 2, valueSuffix: 'm/s' }
        },
        {
            yAxis: 1,
            type: 'areaspline',
            name: 'Battery',
            color: '#5FC671',
            data: [[]],
            dataGrouping: { enabled: false },
            tooltip: { valueDecimals: 2, valueSuffix: '%' }
        },
        {
            yAxis: 2,
            type: 'line',
            step: 'left',
            name: 'Status',
            color: '#B565A7',
            marker: {
                lineWidth: 2,
                fillColor: 'white'
            },
            data: [[]],
            dataGrouping: { enabled: false },
            tooltip: {
                pointFormatter: function (this: Highcharts.Point) {
                    return '<span style="color:' + this.color + '">\u25CF</span> ' + this.series.name + ': <b>' + this.y + '</b><br/>' +
                        'Equal to ' + (this.y === 0 ? 'waiting' : this.y === 1 ? 'running' : 'inactive');
                }

            }
        },
        {
            yAxis: 3,
            type: 'line',
            step: 'left',
            name: 'operation Status',
            color: '#EFC050',
            marker: {
                lineWidth: 2,
                fillColor: 'white'
            },
            data: [[]],
            dataGrouping: { enabled: false },
            tooltip: {
                pointFormatter: function (this: Highcharts.Point) {
                    return '<span style="color:' + this.color + '">\u25CF</span> ' + this.series.name + ': <b>' + this.y + '</b><br/>' +
                        'Equal to : ' + (this.y === 0 ? 'NORMAL' : this.y === 1 ? 'EMS' : 'PAUSE');
                }

            }
        }
        ],
        responsive: {
            rules: [{
                condition: {
                    maxWidth: 500
                },
                chartOptions: {
                    legend: {
                        layout: 'horizontal',
                        align: 'center',
                        verticalAlign: 'bottom'
                    }
                }
            }]
        }
    };

}



        /*this.robotService.getbyNameAllDatabyNameAndDatetime("robot-1", { start: '2024-01-01', end: '2024-04-01' }).subscribe(
            (response) => {}
            , (error) => {
                this.robotService.msgResponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message };
                this.store.dispatch(ShowAlert(this.robotService.msgResponseStatus));
            });*/



        // this.robotService.getbyNameAllDatabyNameAndUnixDatetime(this.nameRobot,{start:1691622800,end:1712975287866}).subscribe(
        /* this.robotService.getAllDatabyName(this.nameRobot).subscribe(
        (response) => {
                 robotState.robotDataChart! = response.body;
                 if (robotState.robotDataChart!.speed.length!=0){this.chart.series[0].setData(robotState.robotDataChart!.speed);}
                 if (robotState.robotDataChart!.battery.length!=0){this.chart.series[1].setData(robotState.robotDataChart!.battery);}
                 if (robotState.robotDataChart!.statusRobot.length!=0){this.chart.series[2].setData(robotState.robotDataChart!.statusRobot);}
                 if (robotState.robotDataChart!.operationStatus.length!=0){this.chart.series[3].setData(robotState.robotDataChart!.operationStatus);}
                 robotState.robotDataChart!.connectionPlotBand.forEach((data) => {
                     this.chart.xAxis[0].addPlotBand({
                         from: data.from,
                         to: data.to,
                         color: "#F7CAC9",
                     });
                 });
                 robotState.robotDataChart!.connectionPlotLine.forEach((data) => {
                     this.chart.xAxis[0].addPlotLine({
                         color: '#000000', width: 1, value: data.value,
                         label: { text: data.text, rotation: (data.text == "CONNECTED" ? 90 : -90), x: (data.text == "CONNECTED" ? 5 : -5), y: (data.text == "CONNECTED" ? 5 : 110) }
                     });
                 });
                 robotState.robotDataChart!.modePlotLine.forEach((data) => {
                     this.chart.xAxis[0].addPlotLine({
                         color: '#000000', width: 1, value: data.value,
                         label: { text: data.text, rotation: (data.text == "AUTO" ? 90 : -90), x: (data.text == "AUTO" ? 5 : -5), y: (data.text == "AUTO" ? 5 : 60) }
                     });
                 });
                 //   robotState.robotDataChart!.modePlotBand.forEach((data) => {
                 //     this.chart.xAxis[0].addPlotBand({
                 //         from: data.from,
                 //         to: data.to,
                 //         color: "#EDD59E"
                 //       });
                 //   });
                 this.chart.hideLoading();
             }
             , (error) => {
                 this.robotService.msgResponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message };
                 this.store.dispatch(ShowAlert(this.robotService.msgResponseStatus));
             });*/
        //     ngAfterViewInit() { while (this.chart.series[0].points.length > 0) {this.chart.series[0].points[0].remove();}
        // while (this.chart.series[1].points.length > 0) {  this.chart.series[1].points[0].remove();}
        // while (this.chart.series[2].points.length > 0) { this.chart.series[2].points[0].remove();}
        //   this.curSubscription = this.mqttClientService.subscribe(this.sub)?.pipe(
        //     debounceTime(1000) //pour retarder l'émission des éléments d'un flux jusqu'à ce qu'aucun nouvel élément ne soit émis pendant un laps de temps spécifié. émet le dernier élément émis après une pause spécifiée
        //  throttleTime(1000) //pour limiter le nombre d'événements émis par l'observable à un maximum d'un toutes les 1000ms.   émet le premier élément et ignore les éléments suivants pendant une période spécifiée.
        //  ).subscribe((message: IMqttMessage) => {
        //   const  updateRobot : RobotDto =JSON.parse(message.payload.toString());
        //    console.log(updateRobot);
        //  const x = (new Date()).getTime();
        //  let y = updateRobot.speed;
        //  //addPoint(options [, redraw] [, shift] [, animation] [, withEvent])
        //  this.chart.series[0].addPoint([x,y], true, false);
        //  y = updateRobot.levelBattery;
        //  this.chart.series[1].addPoint([x,y], true, false);
        //    }); }

        //ngOnDestroy(): void {
            //this.mqttClientService.closeSubscribe(this.curSubscription);}