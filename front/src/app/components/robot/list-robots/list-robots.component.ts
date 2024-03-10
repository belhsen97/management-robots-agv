import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { RobotService } from 'src/app/core/store/services/robot.service';
import { AddRobotComponent } from '../add-robot/add-robot.component';
import { Store } from '@ngrx/store';
import { ShowAlert } from 'src/app/core/store/Global/App.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';

@Component({
  selector: 'app-list-robots',
  templateUrl: './list-robots.component.html',
  styleUrls: ['./list-robots.component.css']
})
export class ListRobotsComponent implements  AfterViewInit, OnDestroy {
  @ViewChild("paginator") paginator!: MatPaginator;
  obs!: Observable<any>;
  dataSource: MatTableDataSource<RobotDto> = new MatTableDataSource<RobotDto>();

  constructor(private store: Store , private changeDetectorRef: ChangeDetectorRef  , public  robotService : RobotService, private dialog: MatDialog ) {}

  ngOnInit() {
    this.dataSource.data = this.robotService.listRobots;
    this.obs = this.dataSource.connect();
    this.robotService.getAll().subscribe(
      (response) => { 
        this.robotService.listRobots = response.body; 
        this.robotService.listRobots.forEach((robot, index) => {
          //robot.no = 1+index;
        });
        this.dataSource.data =    this.robotService.listRobots ;
      }
      ,(error) => {
        this.robotService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        this.store.dispatch( ShowAlert(    this.robotService.msgReponseStatus) ); 
        //this.robotService.goToComponent("/sign-in");
      }) ;




  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.changeDetectorRef.detectChanges(); 
  }
  ngOnDestroy() {
    if (this.dataSource) {
      this.dataSource.disconnect();
    }
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