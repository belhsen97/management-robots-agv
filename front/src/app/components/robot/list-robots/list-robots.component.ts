import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { RobotService } from 'src/app/core/store/services/robot.service';
import { AddRobotComponent } from '../add-robot/add-robot.component';

@Component({
  selector: 'app-list-robots',
  templateUrl: './list-robots.component.html',
  styleUrls: ['./list-robots.component.css']
})
export class ListRobotsComponent implements  AfterViewInit, OnDestroy {
  @ViewChild("paginator") paginator!: MatPaginator;
  @ViewChild("paginator2") paginator2!: MatPaginator;
  obs!: Observable<any>;
  obs2!: Observable<any>;
  dataSource: MatTableDataSource<RobotDto> = new MatTableDataSource<RobotDto>();
  dataSource2: MatTableDataSource<RobotDto> = new MatTableDataSource<RobotDto>();

  constructor(private changeDetectorRef: ChangeDetectorRef  , public  robotService : RobotService, private dialog: MatDialog ) {}

  ngOnInit() {
   // this.changeDetectorRef.detectChanges();
    // this.dataSource.paginator = this.paginator;
    // this.dataSource2.paginator = this.paginator2;
    //console.log(this.dataSource2);

    this.dataSource.data = this.robotService.listRobots;
    this.dataSource2.data = this.robotService.listRobots;
    this.obs = this.dataSource.connect();
    this.obs2 = this.dataSource2.connect();


   //console.log(this.robotService.listRobots);
   //this.dataSource.renderRows();
  // this.dataSource.data = this.robotService.listRobots;
 // this.dataSource.

  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource2.paginator = this.paginator2;
    this.changeDetectorRef.detectChanges(); 
  }
  ngOnDestroy() {
    if (this.dataSource) {
      this.dataSource.disconnect();
    }
    if (this.dataSource2) {
      this.dataSource2.disconnect();
    }
  }




  _setDataSource(indexNumber:any ) {
    setTimeout(() => {
      switch (indexNumber) {
        case 0:
          !this.dataSource.paginator ? this.dataSource.paginator = this.paginator : null;
          break;
        case 1:
          !this.dataSource2.paginator ? this.dataSource2.paginator = this.paginator2 : null;
      }
    });
  }




  onClickAddRobot():void{
    this.openPopupRobot(0, "Add New Robot"); 
  }
  onClickEditRobot(id:any ):void{
    this.openPopupRobot(id,"Edit Robot",true);
  }


  openPopupRobot(id: any, title: any, isedit = false) {
    this.dialog.open(AddRobotComponent, {
      width: '40%',
      data: {
        id: id,
        title: title,
        isedit: isedit
      }
    })
  }


  getStatusClass(status: string): string {
    switch (status) {
      case 'WAITING':
        return 'status-waiting'; // CSS class for WAITING status
      case 'RUNNING':
        return 'status-running'; // CSS class for RUNNING status
      case 'STOP':
        return 'status-stop'; // CSS class for STOP status
      default:
        return 'status-unknown'; // Default CSS class
    }
  }
} 