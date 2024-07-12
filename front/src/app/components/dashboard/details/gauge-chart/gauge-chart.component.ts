import { AfterViewInit, ChangeDetectorRef, Component, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';

import * as Highcharts from 'highcharts/highstock';
import dataInit from 'highcharts/modules/data';
import highchartsMoreInit from 'highcharts/highcharts-more';
import seriesLabelInit from 'highcharts/modules/series-label';
import exportingInit from "highcharts/modules/exporting";
import exportDataInit from "highcharts/modules/export-data";
import accessibilityInit from "highcharts/modules/accessibility";
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Subscribe } from 'src/app/core/store/models/Mqtt/Subscribe.model';
import { Subscription, throttleTime } from 'rxjs';
import { StatusRobot } from 'src/app/core/store/models/Robot/StatusRobot.enum';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';
import { Store } from '@ngrx/store';
import { getConnectionFromRobot, getLevelBatteryFromRobot, getModeFromRobot, getOperationStatusFromRobot, getRobot, getSpeedFromRobot, getStatusRobotFromRobot } from 'src/app/core/store/selectors/Robot.Selector';
import { startListenerRobot, startListenerRobotByProperty, stopListenerRobot, stopListenerRobotByProperty } from 'src/app/core/store/actions/Robot.Action';

dataInit(Highcharts);
highchartsMoreInit(Highcharts);
seriesLabelInit(Highcharts);
exportingInit(Highcharts);
exportDataInit(Highcharts);
accessibilityInit(Highcharts);

@Component({
    selector: 'app-gauge-chart',
    templateUrl: './gauge-chart.component.html',
    styleUrls: ['./gauge-chart.component.css']
})
export class GaugeChartComponent implements OnInit, AfterViewInit, OnDestroy {
    private curSubscription: Subscription | undefined;
   

    private getConnectionFromRobotSub: Subscription | undefined;
    private getModeFromRobotSub: Subscription | undefined;
    private getStatusRobotFromRobotSub: Subscription | undefined;
    private getOperationStatusFromRobotSub: Subscription | undefined;
    private getLevelBatteryFromRobotSub: Subscription | undefined;
    private getSpeedFromRobotSub: Subscription | undefined;

    private chart: any;
    private subTopicRobot: Subscribe = { topic: "topic/data/robot/", qos: 0 };
    private subTopicRobotByProperty: Subscribe = { topic: "topic/data/robot/+/property/+", qos: 0 };
    constructor(/* private mqttClientService :MqttClientService,*/
        private storeRobot: Store<RobotState>,
        private dialogRef: MatDialogRef<GaugeChartComponent>,
        @Inject(MAT_DIALOG_DATA) private data: any) { }

    ngOnInit() {
        if (this.data.name == "") { this.dialogRef.close(null); return; }
        this.chart = Highcharts.chart("gauge-container-robot", this.chartOption);
        this.subTopicRobot.topic += this.data.name;
        this.subTopicRobotByProperty.topic = `topic/data/robot/${this.data.name}/property/+`;


     
        this.getConnectionFromRobotSub = this.storeRobot.select(getConnectionFromRobot).pipe().subscribe(item => {
            console.log("getConnectionFromRobot");
        });
        this.getModeFromRobotSub = this.storeRobot.select(getModeFromRobot).pipe().subscribe(item => {
            console.log("getModeFromRobot");
        });
        this.getStatusRobotFromRobotSub = this.storeRobot.select(getStatusRobotFromRobot).pipe().subscribe(value => {
            console.log("getStatusRobotFromRobot");
            const numCateegory = (value == StatusRobot.RUNNING ? 1 : (value == StatusRobot.WAITING ? 2 : 3));
            this.chart.series[2].setData([numCateegory]);
        });
        this.getOperationStatusFromRobotSub = this.storeRobot.select(getOperationStatusFromRobot).pipe().subscribe(item => {
            console.log("getOperationStatusFromRobot");
        });
        this.getLevelBatteryFromRobotSub = this.storeRobot.select(getLevelBatteryFromRobot).pipe().subscribe(value => {
            this.chart.series[1].setData([parseFloat(value.toFixed(1))]);
        });
        this.getSpeedFromRobotSub = this.storeRobot.select(getSpeedFromRobot).pipe().subscribe(value => {
            this.chart.series[0].setData([parseFloat(value.toFixed(1))]);
        });
        





    }
    ngAfterViewInit() {
        this.storeRobot.dispatch(startListenerRobot({ subscribe: this.subTopicRobot }));
        this.storeRobot.dispatch(startListenerRobotByProperty({ subscribe: this.subTopicRobotByProperty }));
    }
    ngOnDestroy() {
        this.storeRobot.dispatch(stopListenerRobot());
        this.storeRobot.dispatch(stopListenerRobotByProperty());
        this.chart.destroy();
        if (this.getConnectionFromRobotSub) {this.getConnectionFromRobotSub.unsubscribe();}
        if (this.getModeFromRobotSub) {this.getModeFromRobotSub.unsubscribe();}
        if (this.getStatusRobotFromRobotSub) {this.getStatusRobotFromRobotSub.unsubscribe();}
        if (this.getOperationStatusFromRobotSub) {this.getOperationStatusFromRobotSub.unsubscribe();}
        if (this.getLevelBatteryFromRobotSub) {this.getLevelBatteryFromRobotSub.unsubscribe();}
        if (this.getSpeedFromRobotSub) {this.getSpeedFromRobotSub.unsubscribe();}
    }


    //     <mat-card class="example-card col-md-6" *ngFor="let card of obs | async">
    //      <div class="container-gauge"  #chartContainer  id="gauge-container-{{card.name}}"></div> 
    // </mat-card>
    //  this.robotService.getAll().subscribe(
    //     (response) => { 
    //       robotState.listRobots = response.body;
    //       robotState.listRobots.forEach(r => r.createdAt = this.robotService.toDate(r.createdAt.toString()));
    //       this.dataSource.data =    robotState.listRobots ;
    //       this.observer = new IntersectionObserver((entries) => {
    //         entries.forEach(entry => {
    //           if (entry.isIntersecting) {
    //             const chartId = entry.target.getAttribute("id");
    //             this.loadChart(chartId!);
    //             this.observer.unobserve(entry.target);
    //           }
    //         });
    //       });
    //       const chartContainers = document.querySelectorAll(".container-gauge");
    //       chartContainers.forEach(container => {
    //         this.observer.observe(container);
    //       });
    // observer!: IntersectionObserver;
    // loadChart(id:string) :void{ Highcharts.chart(id,this.chartOption);}
    //     }
    //     ,(error) => {
    //       this.robotService.msgResponseStatus  =   { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
    //       this.store.dispatch( ShowAlert(  this.robotService.msgResponseStatus ) ); 
    //       //this.robotService.goToComponent("/sign-in");
    //     }) ;
        //     this.batteryIcon = this.chart.renderer.html(
        //     '<i class="las la-battery-empty" style="font-size:24px;"></i>', // HTML icon content
        //     this.chart.plotLeft +  this.chart.plotWidth / 2 - 16, // X position
        //     this.chart.plotTop +  this.chart.plotHeight / 2 - 16, // Y position   las la-exclamation-triangle
        //     'auto', // Width default auto
        //     'auto', // Height default auto
        //     'battery-icon', // CSS class name
        //     true // Allows HTML
        // ).add();

    //     batteryIcon : any ;
    //    updateBatteryIcon(state : string ) : void {
    //     this.batteryIcon.update({
    //         html: '<i class="fa fa-battery-' + state + '"></i>'
    //     });
    // }


    chartOption: any = {
        chart: {
            type: 'gauge',
            alignTicks: false,
            plotBackgroundColor: null,
            plotBackgroundImage: null,
            plotBorderWidth: 0,
            plotShadow: false
        },
        tooltip: {
            enabled: false
        },
        credits: {
            enabled: false
        },
        title: {
            text: 'Robot- 1 '
        },
        pane: [{
            startAngle: -90,
            endAngle: 90,
            center: ['50%', '50%'],
            size: 300
        }, {
            startAngle: 110,
            endAngle: 250,
            center: ['50%', '50%'],
            size: 300
        }, {
            startAngle: 135,
            endAngle: 225,
            center: ['50%', '50%'],
            size: 300
        }
        ],
        yAxis: [{
            pane: 0,
            min: 0,
            max: 0.5,
            lineColor: '#000000',
            tickColor: '#202020',
            tickInterval: 0.1,
            tickWidth: 2,
            minorTickColor: '#339',
            offset: -5,
            lineWidth: 2,
            labels: {
                distance: -28,
                rotation: 'auto',
                color: '#202020',
                style: { fontSize: '16px' }
            },
            tickLength: 10,
            minorTickLength: 8,
            endOnTick: false,
            plotBands: [{
                from: robotState.settingRobot?.speed.max,
                to: 0.5,
                color: '#CE2C40',
                innerRadius: '95%',
                outerRadius: '105%'
            }, {
                from: robotState.settingRobot?.speed.min,
                to: robotState.settingRobot?.speed.max,
                color: '#338D62',
                innerRadius: '95%',
                outerRadius: '105%'
            },
            {
                from: 0,
                to: robotState.settingRobot?.speed.min,
                color: '#DCC902',
                innerRadius: '96%',
                outerRadius: '105%'
            }
            ],
            plotLines: [{
                value: robotState.settingRobot?.speed.max,
                zIndex: 1,
                width: 1,
                color: '#CE2C40',
                dashStyle: 'longdashdot'
            }],
            title: {
                text: 'Speed m/s',
                style: {
                    color: '#BBB',
                    fontWeight: 'normal',
                    fontSize: '12px',
                    lineHeight: '10px'
                },
                y: 10
            }
        }, {
            pane: 1,
            min: 0,
            max: 100,
            //tickPosition: 'outside',
            lineColor: '#000000',
            tickColor: '#202020',
            offset: -5,
            lineWidth: 2,
            //minorTickPosition: 'outside',
            tickInterval: 25,
            tickWidth: 2,
            minorTickColor: '#933',
            tickLength: 8,
            minorTickLength: 4,
            labels: {
                distance: -28,
                rotation: 'auto',
                color: '#202020',
                style: { fontSize: '16px' }
            },
            endOnTick: false,
            plotBands: [{
                from: 0,
                to: 15,
                color: '#CE2C40',
                innerRadius: '96%',
                outerRadius: '105%'
            }]

        }, {
            pane: 2,
            min: 1,
            max: 3,
            categories: ["", "Run", "Wait", "Inactive"],
            //tickInterval: 1,
            tickWidth: 2,
            tickPosition: 'outside',
            lineColor: '#000000',
            tickColor: '#202020',
            lineWidth: 2,
            minorTickPosition: 'outside',
            minorTickColor: '#933',
            tickLength: 10,
            minorTickLength: 0,
            labels: {
                distance: 24,
                rotation: 0,
                style: { fontSize: '14px', fontWeight: 'bold' }
            },
            offset: -85,
            endOnTick: false,
            title: {
                text: 'Status',
                style: {
                    color: '#BBB',
                    fontWeight: 'normal',
                    fontSize: '12px',
                    lineHeight: '10px'
                },
                y: 110
            }
        }],

        series: [
            {
                yAxis: 0,
                name: 'Speed',
                data: [0],
                dial: {
                    backgroundColor: '#C02316',
                    radius: '90%',
                    baseWidth: 4,
                    baseLength: '90%',
                    rearLength: 0

                },
                dataLabels: {
                    format: '<span style="color:#339">{y} m/s</span><br/>',
                    enabled: true,
                    x: -70,
                    y: -10,
                },
                tooltip: {
                    valueSuffix: ' m/s'
                }
            },
            {
                yAxis: 1,
                name: 'Battery',
                data: [0],
                dial: {
                    backgroundColor: '#C02316',
                    radius: '90%',
                    baseWidth: 4,
                    baseLength: '90%',
                    rearLength: 0

                },
                dataLabels: {
                    x: 80,
                    y: -10,
                    format:
                        '<span style="color:#933">{y} %</span>',
                },
                tooltip: {
                    valueSuffix: ' %'
                }
            },
            {
                yAxis: 2,
                name: 'Status',
                data: [3],
                dial: {
                    backgroundColor: '#202020',
                    radius: '40%',
                    baseWidth: 10,
                    baseLength: '90%',
                    rearLength: 10
                },
                pivot: {
                    radius: 10,
                    backgroundColor: '#202020'
                },
                dataLabels: {
                    enabled: false,
                    x: -70,
                    y: -50,
                    format:
                        '<span style="color:#933">{y} stat</span>'
                },
                tooltip: {
                    valueSuffix: ' Status'
                }
            }]
    };

} 
