import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';

import * as Highcharts from 'highcharts';
//import dataInit from 'highcharts/modules/data';
//import seriesLabelInit from 'highcharts/modules/series-label';
import exportingInit from "highcharts/modules/exporting";
import exportDataInit from "highcharts/modules/export-data";
import accessibilityInit from "highcharts/modules/accessibility";
import { Store } from '@ngrx/store';
import { GlobalState } from 'src/app/core/store/states/Global.state';
import { RobotService } from 'src/app/core/services/robot.service';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';
import { MatDialog } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { getDateRangeSearchInput } from 'src/app/core/store/selectors/global.Selectors';
import { loadDataRobotData } from 'src/app/core/store/actions/Robot.Action';
import { getListRobotDataBand, getRobotDataBand } from 'src/app/core/store/selectors/Robot.Selector';
//import noDataToDisplayInit from 'highcharts/modules/no-data-to-display';
//import annotationsInit from 'highcharts/modules/annotations';
//dataInit(Highcharts);
//seriesLabelInit(Highcharts);
exportingInit(Highcharts);
exportDataInit(Highcharts);
accessibilityInit(Highcharts);
//noDataToDisplayInit(Highcharts);
//annotationsInit(Highcharts);
@Component({
    selector: 'app-charts-robots-statistic',
    templateUrl: './charts-robots-statistic.component.html',
    styleUrls: ['./charts-robots-statistic.component.css']
})
export class ChartsRobotsStatisticComponent implements OnInit, AfterViewInit, OnDestroy {

    private piechart: any; private barchart: any; private linechart: any;
    private getDateRangeSearchInputSub: Subscription | undefined;
    private getlistRobotPropertysSub: Subscription | undefined;



    constructor(private storeRouter: Store,
        private storeGlobal: Store<GlobalState>,
        public robotService: RobotService,
        private storeRobot: Store<RobotState>,
        private dialog: MatDialog) { }


    ngOnInit(): void {
        this.barchart = Highcharts.chart('container-bar-chart', this.chartBarOptions);
        this.piechart = Highcharts.chart('container-pie-chart', this.chartPieOptions);
        this.linechart = Highcharts.chart('container-line-chart', this.chartLineOptions);
        this.getlistRobotPropertysSub = this.storeRobot.select(getListRobotDataBand).subscribe(item => {
            robotState.listRobotDataBand = item;
        });
    }

    ngAfterViewInit(): void {
        this.getDateRangeSearchInputSub = this.storeGlobal.select(getDateRangeSearchInput).subscribe(value => {
            this.storeRobot.dispatch(loadDataRobotData({
                name: null,
                start: (value.start == null ? null : value.start.getTime()),
                end: (value.end == null ? null : value.end.getTime())
            }));
        });
    }

    ngOnDestroy(): void {
        this.barchart.destroy(); this.piechart.destroy(); this.linechart.destroy();
        if (this.getDateRangeSearchInputSub) { this.getDateRangeSearchInputSub.unsubscribe(); }
        if (this.getlistRobotPropertysSub) { this.getlistRobotPropertysSub.unsubscribe(); }
    }

    onChangeSelectTypeProperty(typeProperty: String): void {
        let dataRobots: { title: {bar: any, line: any, pie: any},listName: String[], series: { bar: any[], line: any[], pie: any[] } } 
        = {title: {bar:  {text: 'Frequency of Property',align: 'left'},
         line:  {text: 'Duration of Property'},
         pie:  {text: 'Percent Duration of Property'}},
         listName: [], series: { bar: [], line: [], pie: [] } };

        dataRobots.series.pie.push({ name: 'Percentage', colorByPoint: true, data: [] });
        let total : number = 0 ; 
        switch (typeProperty) {
            case "CONNECTION": {
                dataRobots.series.bar.push({ name: 'Connected', data: [] });
                dataRobots.series.bar.push({ name: 'Disconnected', data: [] });
                dataRobots.series.line.push({ name: 'Connected', data: [] });
                dataRobots.series.line.push({ name: 'Disconnected', data: [] });
                dataRobots.title.bar.text ='Frequency of Connection';
                dataRobots.title.line.text='Duration of Connection';
                dataRobots.title.pie.text ='Percent Duration of Connection';
                robotState.listRobotDataBand!.forEach((item: any) => {
                    total += item.connection.duration.connected;
                });
                break;
            }
            case "MODE_ROBOT": {
                dataRobots.series.bar.push({ name: 'auto', data: [] });
                dataRobots.series.bar.push({ name: 'manual', data: [] });
                dataRobots.series.line.push({ name: 'auto', data: [] });
                dataRobots.series.line.push({ name: 'manual', data: [] });
                dataRobots.title.bar.text ='Frequency of Mode';
                dataRobots.title.line.text='Duration of Mode';
                dataRobots.title.pie.text ='Percent Duration of Mode';
                robotState.listRobotDataBand!.forEach((item: any) => {
                    total += item.mode.duration.auto;
                });
                break;
            }
            case "STATUS_ROBOT": {
                dataRobots.series.bar.push({ name: 'inactive', data: [] });
                dataRobots.series.bar.push({ name: 'running', data: [] });
                dataRobots.series.bar.push({ name: 'waiting', data: [] });
                dataRobots.series.line.push({ name: 'inactive', data: [] });
                dataRobots.series.line.push({ name: 'running', data: [] });
                dataRobots.series.line.push({ name: 'waiting', data: [] });
                dataRobots.title.bar.text ='Frequency of Statut Robot';
                dataRobots.title.line.text='Duration of Statut Robot';
                dataRobots.title.pie.text ='Percent Duration of Statut Robot';
                robotState.listRobotDataBand!.forEach((item: any) => {
                    total += item.statusRobot.duration.running;
                });
                break;
            }
            case "OPERATION_STATUS": {
                dataRobots.series.bar.push({ name: 'ems', data: [] });
                dataRobots.series.bar.push({ name: 'normal', data: [] });
                dataRobots.series.bar.push({ name: 'pause', data: [] });
                dataRobots.series.line.push({ name: 'ems', data: [] });
                dataRobots.series.line.push({ name: 'normal', data: [] });
                dataRobots.series.line.push({ name: 'pause', data: [] });
                dataRobots.title.bar.text ='Frequency of Operation Status';
                dataRobots.title.line.text='Duration of Operation Status';
                dataRobots.title.pie.text ='Percent Duration of Operation Status';
                robotState.listRobotDataBand!.forEach((item: any) => {
                    total += item.operationStatus.duration.normal;
                });
                break;
            }
            case "LEVEL_BATTERY": {
                dataRobots.series.bar.push({ name: 'charge', data: [] });
                dataRobots.series.bar.push({ name: 'discharge', data: [] });
                dataRobots.series.bar.push({ name: 'standby', data: [] });
                dataRobots.series.line.push({ name: 'charge', data: [] });
                dataRobots.series.line.push({ name: 'discharge', data: [] });
                dataRobots.series.line.push({ name: 'standby', data: [] });
                dataRobots.title.bar.text ='Frequency of Battery';
                dataRobots.title.line.text='Duration of Battery';
                dataRobots.title.pie.text ='Percent Duration of Battery';
                robotState.listRobotDataBand!.forEach((item: any) => {
                    total += item.battery.duration.charge;
                });
                break;
            }
            case "SPEED": {
                dataRobots.series.bar.push({ name: 'max', data: [] });
                dataRobots.series.bar.push({ name: 'min', data: [] });
                dataRobots.series.bar.push({ name: 'standby', data: [] });
                dataRobots.series.line.push({ name: 'max', data: [] });
                dataRobots.series.line.push({ name: 'min', data: [] });
                dataRobots.series.line.push({ name: 'standby', data: [] });
                dataRobots.title.bar.text ='Frequency of Speed';
                dataRobots.title.line.text='Duration of Speed';
                dataRobots.title.pie.text ='Percent Duration of Speed';
                robotState.listRobotDataBand!.forEach((item: any) => {
                    total += item.speed.duration.standby;
                });
                break;
            }
            default: {
                dataRobots.series.bar.push({ name: 'effiency', data: [] });
                dataRobots.series.bar.push({ name: 'unreliable', data: [] });
                dataRobots.series.line.push({ name: 'effiency', data: [] });
                dataRobots.series.line.push({ name: 'unreliable', data: [] });
                // robotState.listRobotDataBand!.forEach((item: any) => {
                //     total += item.connection.duration.connected;
                // });
                break;
            }
        }
        for (let i: number = 0; i < robotState.listRobotDataBand!.length; i++) {
            dataRobots.listName.push(robotState.listRobotDataBand![i].name);
            switch (typeProperty) {
                case "CONNECTION": {
                    dataRobots.series.bar[0].data.push(robotState.listRobotDataBand![i].connection.frequency.connected);
                    dataRobots.series.bar[1].data.push(robotState.listRobotDataBand![i].connection.frequency.desconnected);
                    dataRobots.series.line[0].data.push(robotState.listRobotDataBand![i].connection.duration.connected);
                    dataRobots.series.line[1].data.push(robotState.listRobotDataBand![i].connection.duration.desconnected);
                    dataRobots.series.pie[0].data.push({ name: robotState.listRobotDataBand![i].name, y: ((robotState.listRobotDataBand![i].connection.duration.connected/total)*100) });
                    break;
                }
                case "MODE_ROBOT": {
                    dataRobots.series.bar[0].data.push(robotState.listRobotDataBand![i].mode.frequency.auto);
                    dataRobots.series.bar[1].data.push(robotState.listRobotDataBand![i].mode.frequency.manual);
                    dataRobots.series.line[0].data.push(robotState.listRobotDataBand![i].mode.duration.auto);
                    dataRobots.series.line[1].data.push(robotState.listRobotDataBand![i].mode.duration.manual);
                    dataRobots.series.pie[0].data.push({ name: robotState.listRobotDataBand![i].name, y: ((robotState.listRobotDataBand![i].mode.frequency.auto/total)*100) });
                    break;
                }
                case "STATUS_ROBOT": {
                    dataRobots.series.bar[0].data.push(robotState.listRobotDataBand![i].statusRobot.frequency.inactive);
                    dataRobots.series.bar[1].data.push(robotState.listRobotDataBand![i].statusRobot.frequency.running);
                    dataRobots.series.bar[2].data.push(robotState.listRobotDataBand![i].statusRobot.frequency.waiting);
                    dataRobots.series.line[0].data.push(robotState.listRobotDataBand![i].statusRobot.duration.inactive);
                    dataRobots.series.line[1].data.push(robotState.listRobotDataBand![i].statusRobot.duration.running);
                    dataRobots.series.line[2].data.push(robotState.listRobotDataBand![i].statusRobot.duration.waiting);
                    dataRobots.series.pie[0].data.push({ name: robotState.listRobotDataBand![i].name, y: ((robotState.listRobotDataBand![i].statusRobot.duration.running/total)*100) });
                    break;
                }
                case "OPERATION_STATUS": {
                    dataRobots.series.bar[0].data.push(robotState.listRobotDataBand![i].operationStatus.frequency.ems);
                    dataRobots.series.bar[1].data.push(robotState.listRobotDataBand![i].operationStatus.frequency.normal);
                    dataRobots.series.bar[2].data.push(robotState.listRobotDataBand![i].operationStatus.frequency.pause);
                    dataRobots.series.line[0].data.push(robotState.listRobotDataBand![i].operationStatus.duration.ems);
                    dataRobots.series.line[1].data.push(robotState.listRobotDataBand![i].operationStatus.duration.normal);
                    dataRobots.series.line[2].data.push(robotState.listRobotDataBand![i].operationStatus.duration.pause);
                    dataRobots.series.pie[0].data.push({ name: robotState.listRobotDataBand![i].name, y: ((robotState.listRobotDataBand![i].operationStatus.duration.normal/total)*100) });
                    break;
                }
                case "LEVEL_BATTERY": {
                    dataRobots.series.bar[0].data.push(robotState.listRobotDataBand![i].battery.frequency.charge);
                    dataRobots.series.bar[1].data.push(robotState.listRobotDataBand![i].battery.frequency.discharge);
                    dataRobots.series.bar[2].data.push(robotState.listRobotDataBand![i].battery.frequency.standby);
                    dataRobots.series.line[0].data.push(robotState.listRobotDataBand![i].battery.duration.charge);
                    dataRobots.series.line[1].data.push(robotState.listRobotDataBand![i].battery.duration.discharge);
                    dataRobots.series.line[2].data.push(robotState.listRobotDataBand![i].battery.duration.standby);
                    dataRobots.series.pie[0].data.push({ name: robotState.listRobotDataBand![i].name, y: ((robotState.listRobotDataBand![i].battery.duration.charge/total)*100) });
                    break;
                }
                case "SPEED": {
                    dataRobots.series.bar[0].data.push(robotState.listRobotDataBand![i].speed.frequency.max);
                    dataRobots.series.bar[1].data.push(robotState.listRobotDataBand![i].speed.frequency.min);
                    dataRobots.series.bar[2].data.push(robotState.listRobotDataBand![i].speed.frequency.standby);
                    dataRobots.series.line[0].data.push(robotState.listRobotDataBand![i].speed.duration.max);
                    dataRobots.series.line[1].data.push(robotState.listRobotDataBand![i].speed.duration.min);
                    dataRobots.series.line[2].data.push(robotState.listRobotDataBand![i].speed.duration.standby);
                    dataRobots.series.pie[0].data.push({ name: robotState.listRobotDataBand![i].name, y: ((robotState.listRobotDataBand![i].speed.duration.standby/total)*100) });
                    break;
                }
                default: {
                    break;
                }
            }
        }
        this.setPieChartValue(dataRobots);
        this.setBarChartValue(dataRobots);
        this.setLineChartValue(dataRobots);
    }

  




    setBarChartValue(dataRobots: any) {
        console.log(dataRobots);
        this.barchart.update({
            title: dataRobots.title.bar,
            xAxis: {
                categories: dataRobots.listName
            },
            // series: dataRobots.series.bar
        });
        while (this.barchart.series.length) {
            this.barchart.series[0].remove();
        }
        dataRobots.series.bar.forEach((item: any) => {
            this.barchart.addSeries(item);
        });

    }
    setLineChartValue(dataRobots: any) {
        this.linechart.update({
            title:  dataRobots.title.line,
            xAxis: {
                categories: dataRobots.listName
            },
            series: dataRobots.series.line
        });
        while (this.linechart.series.length) {
            this.linechart.series[0].remove();
        }
        dataRobots.series.line.forEach((item: any) => {
            this.linechart.addSeries(item);
        });
    }
    setPieChartValue(dataRobots: any) {
        this.piechart.update({
            title: dataRobots.title.pie,
            series: dataRobots.series.pie
        });
        // let countConnected : number = 0;
        // let countDisconnected : number = 0 
        // for ( let i : number = 0; i <robotState.listRobotDataBand!.length ; i++){
        //   countConnected += robotState.listRobotDataBand![i].connection.duration.connected;
        //   countDisconnected += robotState.listRobotDataBand![i].connection.duration.desconnected;
        // }
        // console.log( countConnected)
        // console.log( countDisconnected)
        // const updatedListRobots = robotState.listRobotDataBand!.map(robot => ({
        //     name:robot.name, y: robot.connection.average.connected 
        // }));
    }









    chartBarOptions: any = {
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Frequency of property'
        },
        xAxis: {
            categories: ['robot-1', 'robot-2', 'robot-3', 'robot-4', 'robot-5', 'robot-6', 'robot-7', 'robot-8', 'robot-9', 'robot-10',
                'robot-11', 'robot-12', 'robot-13', 'robot-14', 'robot-15', 'robot-16', 'robot-17', 'robot-18', 'robot-19', 'robot-20']
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Frequency'
            }
        },
        legend: {
            reversed: true
        },
        plotOptions: {
            series: {
                stacking: 'normal',
                dataLabels: {
                    enabled: true
                }
            }
        },
        series: [
            {
                name: 'item-1',
                data: [4, 4, 6, 15, 12, 4, 4, 6, 15, 12, 4, 4, 6, 15, 12, 4, 4, 6, 15, 12]
            },
            {
                name: 'item-2',
                data: [5, 3, 12, 6, 11, 4, 4, 6, 15, 12, 5, 3, 12, 6, 11, 4, 4, 6, 15, 12]
            },
            {
                name: 'item-3',
                data: [5, 3, 12, 6, 11, 4, 4, 6, 15, 12, 5, 3, 12, 6, 11, 4, 4, 6, 15, 12]
            }]
    };

    chartPieOptions: any = {
        chart: {
            type: 'pie'
        },
        title: {
            text: 'Percent Duration of Property'
        },
        tooltip: {
            valueSuffix: '%'
        },
        // subtitle: {
        //     text:
        //         'Source:<a href="https://www.mdpi.com/2072-6643/11/3/684/htm" target="_default">MDPI</a>'
        // },
        plotOptions: {
            series: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: [{
                    enabled: true,
                    distance: 20
                }, {
                    enabled: true,
                    distance: -40,
                    format: '{point.percentage:.1f}%',
                    style: {
                        fontSize: '1.2em',
                        textOutline: 'none',
                        opacity: 0.7
                    },
                    filter: {
                        operator: '>',
                        property: 'percentage',
                        value: 10
                    }
                }]
            }
        },
        series: [
            {
                name: 'Percentage',
                colorByPoint: true,
                data: [
                    {
                        name: 'Robot-1',
                        y: 0.1
                    },
                    {
                        name: 'Robot-2',
                        //sliced: true,
                        //selected: true,
                        y: 10
                    },
                    {
                        name: 'Robot-3',
                        y: 0.0004
                    }
                ]
            },
        ]
    };

    chartLineOptions: any = {

        title: {
            text: 'Duration of Property',
            align: 'left'
        },

        subtitle: {
            text: 'By Job Category. Source: <a href="https://irecusa.org/programs/solar-jobs-census/" target="_blank">IREC</a>.',
            align: 'left'
        },

        yAxis: {
            title: {
                text: 'Duration'
            },
            labels: {
                formatter: function (this: Highcharts.AxisLabelsFormatterContextObject): any {
                    // Assuming the value is in seconds, convert to HH:mm:ss
                    let totalSeconds = this.value as number / 1000;
                    let days = Math.floor(totalSeconds / (3600 * 24));
                    //let hours = Math.floor(totalSeconds / 3600);
                    let hours = Math.floor((totalSeconds % (3600 * 24)) / 3600);
                    let minutes = Math.floor((totalSeconds % 3600) / 60);
                    let seconds = totalSeconds % 60;
                    return `${days}d ${hours}h ${minutes}m ${seconds}s`;
                }
            }
        },

        xAxis: {
            accessibility: {
                rangeDescription: 'Range: 2010 to 2020'
            }
            , categories: ['robot-1', 'robot-2', 'robot-3', 'robot-4', 'robot-5', 'robot-6', 'robot-7', 'robot-8', 'robot-9', 'robot-10',
                'robot-11', 'robot-12', 'robot-13', 'robot-14', 'robot-15', 'robot-16', 'robot-17', 'robot-18', 'robot-19', 'robot-20']
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle'
        },
        plotOptions: {
            series: {
                label: {
                    connectorAllowed: false
                },
                lineWidth: 0,
                marker: {
                    enabled: true,
                    radius: 4
                },
                states: {
                    hover: {
                        lineWidthPlus: 0
                    }
                }
            }
        },
        series: [{
            name: 'Connected',
            data: [
                43934, 48656, 65165, 81827, 112143, 142383,
                171533, 165174, 155157, 161454, 154610
            ]
        }, {
            name: 'Disconnected',
            data: [
                24916, 37941, 29742, 29851, 32490, 30282,
                38121, 36885, 33726, 34243, 31050
            ]
        }],
        tooltip: {
            formatter: function (this: Highcharts.Point): any {
                // Assuming the value is in milliseconds, convert to HH:mm:ss
                let totalSeconds = Math.floor(this.y! / 1000); // convert milliseconds to seconds
                let days = Math.floor(totalSeconds / (3600 * 24));
                let hours = Math.floor((totalSeconds % (3600 * 24)) / 3600);
                let minutes = Math.floor((totalSeconds % 3600) / 60);
                let seconds = totalSeconds % 60;
                return `${this.series.name}: ${days}d ${hours}h ${minutes}m ${seconds}s`;
            }
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