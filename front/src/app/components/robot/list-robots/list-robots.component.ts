import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { RobotService } from 'src/app/core/services/robot.service';
import { AddRobotComponent } from '../add-robot/add-robot.component';
import { Store } from '@ngrx/store';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';
import { HttpErrorResponse } from '@angular/common/http';
import { getValueSearchInput } from 'src/app/core/store/selectors/global.Selectors';
import { robotState } from 'src/app/core/store/states/Robot.state';
import { wsState } from 'src/app/core/store/states/Worstation.state';

@Component({
  selector: 'app-list-robots',
  templateUrl: './list-robots.component.html',
  styleUrls: ['./list-robots.component.css']
})
export class ListRobotsComponent implements OnInit ,  AfterViewInit, OnDestroy {
  @ViewChild("paginator") paginatorRobot!: MatPaginator;
  obs!: Observable<any>;
  dataSource !: MatTableDataSource<RobotDto>;

  constructor(private store: Store,
    public workstationService:WorkstationService,
    private changeDetectorRef: ChangeDetectorRef,
    public  robotService : RobotService,
    private dialog: MatDialog ){  
     // this.dataSource = robotState.dataSource!;
    }

  ngOnInit() {
  this.dataSource = new MatTableDataSource<RobotDto>();

  this.store.select(getValueSearchInput).subscribe(value => {
    if (value === null || value === undefined || this.dataSource == undefined) { return; }
    this.dataSource.filter = value;
    console.log(value);
  });


    //this.dataSource.data = robotState.listRobots;
    this.obs = this.dataSource.connect();
   // this.dataSourceRobot.data =    robotState.listRobots ;
   this.robotService.getAll().subscribe(
      (response) => { 
        robotState.listRobots = response.body;
        robotState.listRobots.forEach(r => r.createdAt = this.robotService.toDate(r.createdAt.toString()));
        this.dataSource.data =    robotState.listRobots ;
      }
      ,(error) => {
        this.robotService.msgResponseStatus  =   { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        this.store.dispatch( ShowAlert(  this.robotService.msgResponseStatus ) ); 
        //this.robotService.goToComponent("/sign-in");
      }) ;



      this.workstationService.getAll().subscribe(
        (response) => { 
          wsState.listWorkstations = response.body; 
        }
        ,(error) => {
          this.robotService.msgResponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          this.store.dispatch( ShowAlert(  this.robotService.msgResponseStatus ) ); 
          //this.workstationService.goToComponent("/sign-in");
        }) ;



  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginatorRobot;
    this.changeDetectorRef.detectChanges(); 
  }
  ngOnDestroy() {
    if (this.dataSource) {this.dataSource.disconnect(); robotState.dataSource!.paginator = null;}
  }






  onClickAdd():void{
    const dialogRef =  this.openPopupRobot("Add New Robot"); 
    dialogRef.afterClosed().subscribe(result => {
      if (result == null) { return; }
      this.robotService.insert(result).subscribe(
        (response) => { 
          robotState.robot = response.body; 
          robotState.robot.createdAt =   this.robotService.toDate(    robotState.robot.createdAt.toString());
          this.robotService.msgResponseStatus  = {title:"Message",datestamp:new Date(),status:ReponseStatus.SUCCESSFUL,message:"success add new workstation"}; 
          this.store.dispatch( ShowAlert(  this.robotService.msgResponseStatus ) ); 
          robotState.listRobots.push(  robotState.robot );
          this.dataSource.data = robotState.listRobots;
        },
        (error:HttpErrorResponse) => {
          if ((error.status === 406 )||(error.status === 403 )) { this.robotService.msgResponseStatus  = error.error; }
          else {
            this.robotService.msgResponseStatus  =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          }
          this.store.dispatch( ShowAlert(  this.robotService.msgResponseStatus ) );  console.log(error.status) ;
        }  
        );
    });
  }
  onClickEdit(robot:any ):void{
    const dialogRef =  this.openPopupRobot("Edit Robot",robot,true);
    dialogRef.afterClosed().subscribe(result => {
      if (result == null) { return; }
      this.robotService.update(robot.id,result).subscribe(
        (response) => { 
          robotState.robot = response.body; 
          robotState.robot.createdAt =   this.robotService.toDate(    robotState.robot.createdAt.toString());
          this.robotService.msgResponseStatus  = {title:"Message",datestamp:new Date(),status:ReponseStatus.SUCCESSFUL,message:"success add new workstation"}; 
          this.store.dispatch( ShowAlert(   this.robotService.msgResponseStatus ) ); 
          const index = robotState.listRobots.findIndex(robot => robot.id === robotState.robot.id);
          if (index !== -1) {    robotState.listRobots[index] =  robotState.robot;  } 
        //   this.robotService.listRobots.forEach((w) => { if(w.id === id) { w =   this.robotService.workstation ;} });

          this.dataSource.data = robotState.listRobots;
        },
        (error:HttpErrorResponse) => {
          if ((error.status === 406 )||(error.status === 403 )) {  this.robotService.msgResponseStatus = error.error; }
          else {
            this.robotService.msgResponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          }
          this.store.dispatch( ShowAlert(  this.robotService.msgResponseStatus ) );  console.log(error.status) ;
        }  
        );
    });
  }
  onClickDelete(id: any): void {
    this.openDialogConfirmation('Confirmation', '  Would you want to delete work station equal ' + id + ' ?', '300ms', '500ms',
      () => {
        this.robotService.delete(id).subscribe(
          (response) => {
            this.robotService.msgResponseStatus  = response.body;
            this.store.dispatch(ShowAlert( this.robotService.msgResponseStatus ));
            robotState.listRobots = robotState.listRobots.filter(item => item.id !== id);
            this.dataSource.data = robotState.listRobots;
          },
          (error: HttpErrorResponse) => {
            if ((error.status === 406) || (error.status === 403)) {  this.robotService.msgResponseStatus  = error.error; }
            else {
              this.robotService.msgResponseStatus  = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
            }
            this.store.dispatch(ShowAlert( this.robotService.msgResponseStatus )); console.log(error.status);
          }
        );

      });
  }


  //Msg box
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
  openPopupRobot(title: any, element: any = undefined, isedit = false) {
    const dialogRef = this.dialog.open(AddRobotComponent, {
      width: '40%',
      data: {element: element,title: title,isedit: isedit}
    });
    return dialogRef;
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