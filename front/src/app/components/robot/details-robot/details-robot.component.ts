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
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';
import { deleteRobot, loadRobotByName, updateRobot, loadDataRobotData } from 'src/app/core/store/actions/Robot.Action';
import { getRobot, getRobotDataBand } from 'src/app/core/store/selectors/Robot.Selector';
import { RobotProperty } from 'src/app/core/store/models/Robot/RobotProperty.model';
import { MatPaginator } from '@angular/material/paginator';
import { GlobalState } from 'src/app/core/store/states/Global.state';
import { getDateRangeSearchInput, getValueSearchInput } from 'src/app/core/store/selectors/global.Selectors';
import { MatDialog } from '@angular/material/dialog';
import { AddRobotComponent } from '../add-robot/add-robot.component';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';
import { Plot } from 'src/app/core/store/models/Robot/RobotDataBand.model';

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
  private getlistRobotPropertysSub: Subscription | undefined;
  private getRobotSub: Subscription | undefined;
  private getValueSearchInputSub: Subscription | undefined;
  private getDateRangeSearchInputSubb: Subscription | undefined;
  private chart: any;
  private nameRobot: string = "";
  public robot !: RobotDto;
  public typeProperty: string = "";

  displayedColumns: string[] = ['text', 'from', 'to'];
  dataSource !: MatTableDataSource<Plot>;
  @ViewChild(MatSort) sort  !: MatSort;
  @ViewChild("paginatorRobotProperty") paginator  !: MatPaginator;

  constructor(private storeRouter: Store,
    private storeGlobal: Store<GlobalState>,
    public robotService: RobotService,
    private storeRobot: Store<RobotState>,
    private dialog: MatDialog) {}
  ngOnInit(): void {
    this.getRouterNameSub = this.storeRouter.select(getRouterName).subscribe(item => {
      console.log(item);
      if (item === null || item === undefined) { return; }
      this.nameRobot = item;
    });
    this.getValueSearchInputSub = this.storeGlobal.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSource == undefined) { return; }
      this.dataSource.filter = value;
    });
    this.getDateRangeSearchInputSubb = this.storeGlobal.select(getDateRangeSearchInput).subscribe(value => { 
      this.storeRobot.dispatch(loadDataRobotData({
        name:(this.nameRobot== null ? null : this.nameRobot ),
        start:(value.start == null ? null : value.start.getTime()),
        end:(value.end == null ? null : value.end.getTime())
      }));
    });
    if (this.getRouterNameSub) { this.getRouterNameSub.unsubscribe(); }
    if (this.nameRobot == "") { this.robotService.goToComponent("dashboard/table"); }





    this.chart = Highcharts.chart('container-pie-chart-robot-details', this.chartOptions);
    this.dataSource = new MatTableDataSource<Plot>();

    this.getlistRobotPropertysSub = this.storeRobot.select(getRobotDataBand).subscribe(item => {
      robotState.robotDataBand = item;
      let dataPlot: Plot[] = [];
      dataPlot = [...dataPlot, ...item!.connection.interval.connected];
      dataPlot = [...dataPlot, ...item!.connection.interval.desconnected];
      dataPlot = [...dataPlot, ...item!.mode.interval.auto];
      dataPlot = [...dataPlot, ...item!.mode.interval.manual];
      dataPlot = [...dataPlot, ...item!.operationStatus.interval.ems];
      dataPlot = [...dataPlot, ...item!.operationStatus.interval.normal];
      dataPlot = [...dataPlot, ...item!.operationStatus.interval.pause];
      dataPlot = [...dataPlot, ...item!.statusRobot.interval.inactive];
      dataPlot = [...dataPlot, ...item!.statusRobot.interval.running];
      dataPlot = [...dataPlot, ...item!.statusRobot.interval.waiting];
      dataPlot = [...dataPlot, ...item!.battery.interval.charge];
      dataPlot = [...dataPlot, ...item!.battery.interval.discharge];
      dataPlot = [...dataPlot, ...item!.battery.interval.standby];
      dataPlot = [...dataPlot, ...item!.speed.interval.max];
      dataPlot = [...dataPlot, ...item!.speed.interval.min];
      dataPlot = [...dataPlot, ...item!.speed.interval.standby];
      this.dataSource.data = dataPlot;
      this.chart.series[0].setData([
        { name: 'connected', y: robotState.robotDataBand!.connection.average.connected },
        { name: 'desconnected', y: robotState.robotDataBand!.connection.average.desconnected }
      ]);
    });
    this.getRobotSub = this.storeRobot.select(getRobot).subscribe(item => { this.robot = item!; });
  }


  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.storeRobot.dispatch(loadRobotByName({ name: this.nameRobot }));
    this.storeRobot.dispatch(loadDataRobotData({ 
      name:(this.nameRobot== null ? null : this.nameRobot )
      ,start:null,end:null
    }));

  }
  ngOnDestroy(): void {
    this.chart.destroy();
    if (this.getRouterNameSub) { this.getRouterNameSub.unsubscribe(); }
    if (this.getlistRobotPropertysSub) { this.getlistRobotPropertysSub.unsubscribe(); }
    if (this.getRobotSub) { this.getRobotSub.unsubscribe(); }
    if (this.getValueSearchInputSub) { this.getValueSearchInputSub.unsubscribe(); }
    if (this.getDateRangeSearchInputSubb) { this.getDateRangeSearchInputSubb.unsubscribe(); }
    if (this.dataSource) { this.dataSource.disconnect(); }
  }


  onClickRefresh(): void { }
  onToggleButtonClickTypeProperty(): void {
    //console.log (this.typeProperty );
    //this.dataSource.filter = this.typeProperty
    switch (this.typeProperty) {
      case "CONNECTION": {
        this.dataSource.data = [...robotState.robotDataBand!.connection.interval.desconnected,
        ...robotState.robotDataBand!.connection.interval.connected];
        this.chart.setTitle({text: "Robot property"},{text: "Connection"});
        this.chart.series[0].setData([
          { name: 'connected', y: robotState.robotDataBand!.connection.average.connected },
          { name: 'desconnected', y: robotState.robotDataBand!.connection.average.desconnected }
        ]);
        break;
      }
      case "MODE_ROBOT": {
        this.dataSource.data = [...robotState.robotDataBand!.mode.interval.auto,
        ...robotState.robotDataBand!.mode.interval.manual];
        this.chart.setTitle({text: "Robot property"},{text: "Mode"});
        this.chart.series[0].setData([
          { name: 'auto', y: robotState.robotDataBand!.mode.average.auto },
          { name: 'manual', y: robotState.robotDataBand!.mode.average.manual }
        ]);
        break;
      }
      case "STATUS_ROBOT": {
        this.dataSource.data = [...robotState.robotDataBand!.statusRobot.interval.inactive,
        ...robotState.robotDataBand!.statusRobot.interval.waiting,
        ...robotState.robotDataBand!.statusRobot.interval.running];
        this.chart.setTitle({text: "Robot property"},{text: "Status Robot"});
        this.chart.series[0].setData([
          { name: 'auto', y: robotState.robotDataBand!.statusRobot.average.inactive },
          { name: 'waiting', y: robotState.robotDataBand!.statusRobot.average.waiting },
          { name: 'running', y: robotState.robotDataBand!.statusRobot.average.running }
        ]);
        break;
      }
      case "OPERATION_STATUS": {
        this.dataSource.data = [...robotState.robotDataBand!.operationStatus.interval.ems,
        ...robotState.robotDataBand!.operationStatus.interval.pause,
        ...robotState.robotDataBand!.operationStatus.interval.normal];
        this.chart.setTitle({text: "Robot property"},{text: "Operation Status"});
        this.chart.series[0].setData([
          { name: 'ems', y: robotState.robotDataBand!.operationStatus.average.ems },
          { name: 'pause', y: robotState.robotDataBand!.operationStatus.average.pause },
          { name: 'normal', y: robotState.robotDataBand!.operationStatus.average.normal }
        ]);
        break;
      }
      case "LEVEL_BATTERY": {
        this.dataSource.data = [...robotState.robotDataBand!.battery.interval.discharge,
        ...robotState.robotDataBand!.battery.interval.standby,
        ...robotState.robotDataBand!.battery.interval.charge];
        this.chart.setTitle({text: "Robot property"},{text: "Battery"});
        this.chart.series[0].setData([
          { name: 'discharge', y: robotState.robotDataBand!.battery.average.discharge },
          { name: 'standby', y: robotState.robotDataBand!.battery.average.standby },
          { name: 'charge', y: robotState.robotDataBand!.battery.average.charge }
        ]);
        break;
      }
      case "SPEED": {
        this.dataSource.data = [...robotState.robotDataBand!.speed.interval.max,
        ...robotState.robotDataBand!.speed.interval.min,
        ...robotState.robotDataBand!.speed.interval.standby];
        this.chart.setTitle({text: "Robot property"},{text: "Speed"});
        this.chart.series[0].setData([
          { name: 'max', y: robotState.robotDataBand!.speed.average.max },
          { name: 'min', y: robotState.robotDataBand!.speed.average.min },
          { name: 'standby', y: robotState.robotDataBand!.speed.average.standby }
        ]);
        break;
      }
      default: {

        break;
      }
    }
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













  chartOptions: any = {
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
