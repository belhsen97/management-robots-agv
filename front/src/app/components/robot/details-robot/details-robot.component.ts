import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
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
import { RobotState } from 'src/app/core/store/states/Robot.state';
import { deleteRobot, loadDataRobotPropertybyName, loadRobotByName, updateRobot } from 'src/app/core/store/actions/Robot.Action';
import { getRobot, getlistRobotPropertys } from 'src/app/core/store/selectors/Robot.Selector';
import { RobotProperty } from 'src/app/core/store/models/Robot/RobotProperty.model';
import { MatPaginator } from '@angular/material/paginator';
import { GlobalState } from 'src/app/core/store/states/Global.state';
import { getValueSearchInput } from 'src/app/core/store/selectors/global.Selectors';
import { MatDialog } from '@angular/material/dialog';
import { AddRobotComponent } from '../add-robot/add-robot.component';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';

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
    private getSelectorRobotSub: Subscription | undefined;
    private getValueSearchInputSub: Subscription | undefined;
    private chart: any;
    private nameRobot: string = "";
    public  robot !: RobotDto;
    public typeProperty: string = "";

    displayedColumns: string[] = [ 'type' , 'value' , 'timestamp'];
    dataSource !: MatTableDataSource<RobotProperty>;
    @ViewChild(MatSort) sort  !: MatSort;
    @ViewChild("paginatorRobotProperty") paginator  !: MatPaginator;
    
constructor(private storeRouter: Store,
    private storeGlobal: Store<GlobalState>,
    public robotService: RobotService,
    private storeRobot: Store<RobotState>,
    private dialog: MatDialog){

}
  ngOnInit(): void {
    this.getRouterNameSub = this.storeRouter.select(getRouterName).subscribe(item => {
        console.log(item)
        if (item === null || item === undefined) { return; }
        this.nameRobot = item;
    });
    this.getValueSearchInputSub = this.storeGlobal.select(getValueSearchInput).subscribe(value => {
        if (value === null || value === undefined || this.dataSource == undefined ){return ; }
         this.dataSource.filter = value;
      });
    if (this.getRouterNameSub) { this.getRouterNameSub.unsubscribe(); }
    if (this.nameRobot == "") {this.robotService.goToComponent("dashboard/table");}





    this.chart = Highcharts.chart('container-pie-chart-robot-details', this.chartOptions); 
    this.dataSource = new MatTableDataSource<RobotProperty>();
    this.getSelectorRobotSub = this.storeRobot.select(getlistRobotPropertys).subscribe(item => {
       this.dataSource.data = item!;
        console.log(item) 
        //this.dataSource.filter = "CONNECTION STATUS_ROBOT";
       /* this.dataSource.filterPredicate =
        (data: RobotProperty, filters: string) => {
          const matchFilter : any = [];
          const filterArray = filters.split('+');
          console.log(filterArray)
          const columns  = (<any>Object).values(data);
          // OR be more specific [data.name, data.race, data.color];
    
          // Main
          filterArray.forEach(filter => {
            const customFilter : any = [];
            columns.forEach((column : any)  => customFilter.push(column.toLowerCase().includes(filter)));
            matchFilter.push(customFilter.some(Boolean)); // OR
          });
          return matchFilter.every(Boolean); // AND
        }*/
      });
      this.getSelectorRobotSub = this.storeRobot.select(getRobot).subscribe(item => { this.robot = item!; });
  }
 

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.storeRobot.dispatch(loadRobotByName({ name: this.nameRobot }));
    this.storeRobot.dispatch(loadDataRobotPropertybyName({ name: this.nameRobot }));
  
  }
  ngOnDestroy(): void {
        this.chart.destroy();
        if (this.getRouterNameSub) { this.getRouterNameSub.unsubscribe(); }
        if (this.getSelectorRobotSub) { this.getSelectorRobotSub.unsubscribe(); } 
        if (this.getValueSearchInputSub) { this.getValueSearchInputSub.unsubscribe(); } 
        if (this.dataSource) { this.dataSource.disconnect(); } 
    }

  
    onClickRefresh():void{}
    onToggleButtonClickTypeProperty():void{ 
        console.log (this.typeProperty )
        this.dataSource.filter = this.typeProperty
    }
    
    onClickEdit(robot: any): void {
        const dialogRef = this.openPopupRobot("Edit Robot", robot, true);
        dialogRef.afterClosed().subscribe(result => {
          if (result == null) { return; }
          this.storeRobot.dispatch(updateRobot({ id: robot.id, robotinput: result }));
        });
      }
      onClickDelete(id: any): void {
        this.openDialogConfirmation('Confirmation', '  Would you want to delete work station equal ' + id + ' ?', '300ms', '500ms',
          () => {
            this.storeRobot.dispatch(deleteRobot({ id: id }));
          });
      }
    openPopupRobot(title: any, element: any = undefined, isedit = false) {
        const dialogRef = this.dialog.open(AddRobotComponent, {
          width: '40%',
          data: { element: element, title: title, isedit: isedit }
        });
        return dialogRef;
      }

      openDialogConfirmation(title: string, message: string, enterAnimationDuration: string,
        exitAnimationDuration: string, callback: () => void): void {
        const dialogRef = this.dialog.open(MessageBoxConfirmationComponent, {
          width: '400px',
          // height: '400px',
          data: { title: title, message: message },
          enterAnimationDuration,
          exitAnimationDuration,
          disableClose: false
        });
        dialogRef.afterClosed().subscribe(result => {
          if (result) {
            callback();
          }
        });
      }













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
                    name: 'WAITING',
                    y: 50
                },
                {
                    name: 'RUNNING',
                    //sliced: true,
                    //selected: true,
                    y: 25
                },
                {
                    name: 'INACTIVE',
                    y: 25
                }
            ]
        }
    ]
};











}
