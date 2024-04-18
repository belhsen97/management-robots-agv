import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import * as Highcharts from 'highcharts/highstock';
import dataInit from 'highcharts/modules/data';
import seriesLabelInit from 'highcharts/modules/series-label';
import exportingInit from "highcharts/modules/exporting";
import exportDataInit from "highcharts/modules/export-data";
import accessibilityInit from "highcharts/modules/accessibility";
import noDataToDisplayInit from 'highcharts/modules/no-data-to-display';
import annotationsInit from 'highcharts/modules/annotations';

import { RobotService } from 'src/app/core/services/robot.service';
import { Subscription } from 'rxjs';
import { getRouterName } from 'src/app/core/store/selectors/Router.Seletor';
import { Store } from '@ngrx/store';
import { MatTableDataSource } from '@angular/material/table';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { MatSort } from '@angular/material/sort';

dataInit(Highcharts);
seriesLabelInit(Highcharts);
exportingInit(Highcharts);
exportDataInit(Highcharts);
accessibilityInit(Highcharts);
noDataToDisplayInit(Highcharts);
annotationsInit(Highcharts);
@Component({
  selector: 'app-details-robot',
  templateUrl: './details-robot.component.html',
  styleUrls: ['./details-robot.component.css']
})
export class DetailsRobotComponent implements OnInit, AfterViewInit, OnDestroy {
    private getRouterNameSub: Subscription | undefined;
    private chart: any;
    private nameRobot: String = "";


    displayedColumns: string[] = ['name', 'statusRobot', 'modeRobot', 'connection', 'operationStatus'];
    dataSource !: MatTableDataSource<RobotDto>;
    @ViewChild(MatSort) sort  !: MatSort;

constructor(private storeRouter: Store, public robotService: RobotService){

}
  ngOnInit(): void {
    this.getRouterNameSub = this.storeRouter.select(getRouterName).subscribe(item => {
        console.log(item)
        if (item === null || item === undefined) { return; }
        this.nameRobot = item;
    });
    if (this.getRouterNameSub) { this.getRouterNameSub.unsubscribe(); }
    if (this.nameRobot == "") {this.robotService.goToComponent("dashboard/table");}

    this.chart = Highcharts.chart('container-pie-chart-robot-details', this.chartOptions);



    this.dataSource = new MatTableDataSource<RobotDto>();


  }
 

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
  }
  ngOnDestroy(): void {
        this.chart.destroy();
        if (this.getRouterNameSub) { this.getRouterNameSub.unsubscribe(); }
        if (this.dataSource) { this.dataSource.disconnect(); } 
    }

  
    onClickRefresh():void{}



























  chartOptions: any =  {
    chart: {
        type: 'pie'
    },
    title: {
        text: 'Robot property'
    },
    tooltip: {
        valueSuffix: '%'
    },
    subtitle: {
        text:
        'Source:<a href="https://www.mdpi.com/2072-6643/11/3/684/htm" target="_default">MDPI</a>'
    },
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
                    name: 'Water',
                    y: 55.02
                },
                {
                    name: 'Fat',
                    //sliced: true,
                    //selected: true,
                    y: 26.71
                },
                {
                    name: 'Carbohydrates',
                    y: 1.09
                },
                {
                    name: 'Protein',
                    y: 15.5
                },
                {
                    name: 'Ash',
                    y: 1.68
                }
            ]
        }
    ]
};











}
