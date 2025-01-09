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
import { getRouterName } from 'src/app/core/store/selectors/Router.seletor';
import { MqttClientService } from 'src/app/core/services/mqtt-client.service';
import { Subscribe } from 'src/app/core/store/models/Mqtt/Subscribe.model';
import { getConnectionFromRobot, getDataRobotChart, getLevelBatteryFromRobot, getModeFromRobot, getOperationStatusFromRobot, getRobot, getSpeedFromRobot, getStatusRobotFromRobot } from 'src/app/core/store/selectors/Robot.selector';
import {  loadDataRobotChart, startListenerRobot, startListenerRobotByProperty, stopListenerRobot, stopListenerRobotByProperty } from 'src/app/core/store/actions/Robot.Action';
import { StatusRobot } from 'src/app/core/store/models/Robot/StatusRobot.enum';
import { OperationStatus } from 'src/app/core/store/models/Robot/OperationStatus.enum';
import { Connection } from 'src/app/core/store/models/Robot/Connection.enum';
import { ModeRobot } from 'src/app/core/store/models/Robot/ModeRobot.enum';
import { RobotDataChart } from 'src/app/core/store/models/Robot/RobotDataChart.model';

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

    private getConnectionFromRobotSub: Subscription | undefined;
    private getModeFromRobotSub: Subscription | undefined;
    private getStatusRobotFromRobotSub: Subscription | undefined;
    private getOperationStatusFromRobotSub: Subscription | undefined;
    private getLevelBatteryFromRobotSub: Subscription | undefined;
    private getSpeedFromRobotSub: Subscription | undefined;






    private subTopicRobot: Subscribe = { topic: "topic/data/robot/", qos: 0 };
    private subTopicRobotByProperty: Subscribe = { topic: "topic/data/robot/+/property/+", qos: 0 };
    private nameRobot: String = "";
    private chart: any;
    private connectionState : Connection = Connection.DISCONNECTED;
    private modeState : ModeRobot = ModeRobot.MANUAL;


    constructor(private storeRouter: Store, 
        public robotService: RobotService,
        private storeRobot: Store<RobotState>,
    ) { }

    ngOnInit(): void {
        this.getRouterNameSub = this.storeRouter.select(getRouterName).subscribe(item => {
            if (item === null || item === undefined) { return; }
            this.nameRobot = item;
        });
        if (this.getRouterNameSub) { this.getRouterNameSub.unsubscribe(); }
        if (this.nameRobot == "") {this.robotService.goToComponent("dashboard/table");}

        this.subTopicRobot.topic += this.nameRobot;
        this.subTopicRobotByProperty.topic = `topic/data/robot/${this.nameRobot}/property/+`;



   
      this.robotService.geDataChartRobot({ name: this.nameRobot }).subscribe(
      (response) => { 
      const  data = response.body as RobotDataChart;
      if (data.speed.length !== 0) {
         data.speed.push([Date.now(),null]);
         //   this.chartOptions.navigator.series.data=data.speed; 
         this.chartOptions.navigator.series[0].data=data.speed; 
         this.chartOptions.series[0].data=data.speed; 
        }
      if (data.battery.length !== 0) {
          //  data.battery.push([Date.now(),null]);
            this.chartOptions.series[1].data=data.battery; 
            this.chartOptions.navigator.series[1].data=data.battery; 
           }
      if (data.statusRobot.length !== 0) { 
         // data.statusRobot.push([Date.now(),null]);
         this.chartOptions.series[2].data = data.statusRobot; 
         this.chartOptions.navigator.series[2].data=data.statusRobot; 
        }
      if (data.operationStatus.length !== 0) { 
        // data.operationStatus.push([Date.now(),null]);
         this.chartOptions.series[3].data=data.operationStatus;
         this.chartOptions.navigator.series[3].data=data.operationStatus; 
        }
       data.connectionPlotBand.forEach((element) => {
           this.chartOptions.xAxis.plotBands.push({
                from: element.from,
                to: element.to,
                color: "#F7CAC9",
                zIndex: 2,
           });
        });
        data.modePlotBand.forEach((element) => {
            this.chartOptions.xAxis.plotBands.push({
                from: element.from,
                to: element.to,
                color: "#e5e8e8",
                zIndex: 1,
            });
        });
        data.connectionPlotLine.forEach((element) => {
            this.chartOptions.xAxis.plotLines.push({
                color: '#000000', width: 1, value: element.value,  zIndex: 3,
                label: { text: element.text, rotation: (element.text == "CONNECTED" ? 90 : -90), x: (element.text == "CONNECTED" ? 5 : -5), y: (element.text == "CONNECTED" ? 5 : 110) }
            });
        });
        data.modePlotLine.forEach((element) => {
            this.chartOptions.xAxis.plotLines.push({
                color: '#000000', width: 1, value: element.value,  zIndex: 3,
                label: {  verticalAlign: 'bottom',text: element.text, rotation: (element.text == "AUTO" ? 90 : -90), x: (element.text == "AUTO" ? 5 : -5) }
            });
        });
      this.chartOptions.xAxis.events = {   afterSetExtremes: (e: any) => this.afterSetExtremes(e) };
      this.chart = Highcharts.stockChart('container-stock-chart-robot',   this.chartOptions );
      });

    }


  
    ngAfterViewInit() {
    }

    ngOnDestroy(): void {
        this.chart.destroy();
    } 


     afterSetExtremes(e:any): void{
        const { chart } = e.target;
        chart.showLoading('Loading data from server...');
        this.robotService.geDataChartRobot({ name:  this.nameRobot, start:Math.round(e.min) ,end:Math.round(e.max) }).subscribe(
            (response) => { 
            const data = response.body as RobotDataChart;
                       chart.series[0].setData(data.speed);
                       chart.series[1].setData(data.battery);
                       chart.series[2].setData(data.statusRobot);
                       chart.series[3].setData(data.operationStatus);

                       let listPlotBands: { from: number; to: number; color: string; zIndex: number; }[] = [];
                       let listPlotLines: { color: string; width: number; value: number; zIndex: number; label: { text: String; rotation: number; x: number; y: number; } | { verticalAlign: string; text: String; rotation: number; x: number; }; }[] = [];
                       data.connectionPlotBand.forEach((element) => {
                        listPlotBands.push({from: element.from, to: element.to, color: "#F7CAC9",zIndex: 1 });
                       });
                       data.modePlotBand.forEach((element) => {
                        listPlotBands.push({from: element.from, to: element.to, color: "#e5e8e8",zIndex: 1 });
                    });
                      data.connectionPlotLine.forEach((element) => {
                        listPlotLines.push({
                               color: '#000000', width: 1, value: element.value,  zIndex: 3,label: { text: element.text, rotation: (element.text == "CONNECTED" ? 90 : -90), x: (element.text == "CONNECTED" ? 5 : -5), y: (element.text == "CONNECTED" ? 5 : 110) } });
                        });
                        data.modePlotLine.forEach((element) => {
                            listPlotLines.push({
                                color: '#000000', width: 1, value: element.value,  zIndex: 3, label: {  verticalAlign: 'bottom',text: element.text, rotation: (element.text == "AUTO" ? 90 : -90), x: (element.text == "AUTO" ? 5 : -5) }
                            });
                        });
                    chart.yAxis[0].update({ plotBands: listPlotBands , plotLines : listPlotLines});
                    chart.hideLoading();
            });
    }

    chartOptions: any = {
        chart: {
            type: 'candlestick',
            zooming: {
                type: 'x'
            },
        },

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


        navigator: {
            enabled: true, height: 50, margin: 10,
            maskFill: 'rgba(180, 198, 220, 0.75)',
            adaptToUpdatedData: false,
       
            series: [
                     //data:  [[]],
                {
                    type: 'spline',
                    name: 'Speed',
                    data: [] 
                },
                {
                    type: 'areaspline',
                    name: 'Battery',
                    data: [] 
                },
                {
                    type: 'line',
                    name: 'Status',
                    step: 'left',
                    data: [] 
                },
                {
                    type: 'line',
                    name: 'Operation Status',
                    step: 'left',
                    data: [] 
                }
            ]
        },

        scrollbar: {
            liveRedraw: false
        },


        rangeSelector: {
            floating: false,
            inputEnabled: true,
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
            selected: 4 ,// all
            buttons: [{
                type: 'hour',
                count: 1,
                text: '1h'
            }, {
                type: 'day',
                count: 1,
                text: '1d'
            }, {
                type: 'month',
                count: 1,
                text: '1m'
            }, {
                type: 'year',
                count: 1,
                text: '1y'
            }, {
                type: 'all',
                text: 'All'
            }],

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










        xAxis: {
            events: {
                afterSetExtremes: null
            },
            minRange: 3600 * 1000, // one hour
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
            , plotLines: [
            //     {value: 1647523800000,color: '#000000',width: 1, label: { text: 'Plot line', rotation: -90, x: -5, y: 50 },
            //     align: 'center',x: 0,y:-10, rotation: -90}, 
            // {value: 1647869400000,color: '#000000',width: 1}
        ],
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
        ]

  
    };












    chartOptionsxxxx: any = {

        plotOptions: {
            series: {
                animation: {
                    duration: 4000
                }
            }
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
                }, {
                    type: 'day',
                    count: 7,
                    text: '1w'
                }
            ], buttonTheme: { width: 30 },
            selected: 1, allButtonsEnabled: true
        },

    
        
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