import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { getValueSearchInput } from 'src/app/core/store/Global/App.Selectors';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { RobotService } from 'src/app/core/store/services/robot.service';

@Component({
  selector: 'app-table-dashboard',
  templateUrl: './table-dashboard.component.html',
  styleUrls: ['./table-dashboard.component.css']
})
export class TableDashboardComponent implements OnInit , AfterViewInit{

  constructor(private store: Store ,public robotService: RobotService){

  }
  displayedColumns: string[] = ['name', 'statusRobot', 'modeRobot', 'connection', 'operationStatus', 'levelBattery', 'speed'];
  //dataSource = this.robotService.listRobots;
  dataSource:any;


  @ViewChild(MatSort) sort  !: MatSort;


  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<RobotDto>(this.robotService.listRobots);
    this.store.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSource == undefined ){return ; }
      this.dataSource.filter = value;
      console.log(  value );
    });
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;

    
    // var timeinterval = setInterval(() => {
    //     console.log("refresh table data");
    //   this.robotService.randomDataRobots();
    //   this.robotService.refreshValuesPanelRobot();
    //   this.dataSource.data = this.robotService.listRobots;
    // }, 2000);
  }
 

 onClickStopAllRobot():void{

 }
 onClickStartAllRobot():void{
  
 }
 onClickTrunOffAllRobot():void{
  
 }

  cellColorLevelBattery(value:number):string{
    return (value < 20 ? 'cell-danger' : value >= 20 && value < 80 ? 'cell-warning' :'cell-steady');
  }
  cellColorSpeed(value:number):string{
    return (value > this.robotService.settingRobot.speed.max || value < this.robotService.settingRobot.speed.min ? 'cell-danger' : 'cell-steady');
  }
}


 