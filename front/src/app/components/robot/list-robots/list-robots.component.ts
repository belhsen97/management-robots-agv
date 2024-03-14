import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { RobotService } from 'src/app/core/services/robot.service';
import { AddRobotComponent } from '../add-robot/add-robot.component';
import { Store } from '@ngrx/store';
import { ShowAlert } from 'src/app/core/store/Global/App.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-list-robots',
  templateUrl: './list-robots.component.html',
  styleUrls: ['./list-robots.component.css']
})
export class ListRobotsComponent implements OnInit ,  AfterViewInit, OnDestroy {
  @ViewChild("paginator") paginatorRobot!: MatPaginator;
  obs!: Observable<any>;
  dataSourceRobot !: MatTableDataSource<RobotDto>;

  constructor(private store: Store,
    public workstationService:WorkstationService,
    private changeDetectorRef: ChangeDetectorRef,
    public  robotService : RobotService,
    private dialog: MatDialog ){}

  ngOnInit() {
   this.dataSourceRobot = new MatTableDataSource<RobotDto>();

    this.dataSourceRobot.data = this.robotService.listRobots;
    this.obs = this.dataSourceRobot.connect();
    this.robotService.getAll().subscribe(
      (response) => { 
        this.robotService.listRobots = response.body;
        this.dataSourceRobot.data =    this.robotService.listRobots ;
      }
      ,(error) => {
        this.robotService.msgReponseStatus =   { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        this.store.dispatch( ShowAlert(    this.robotService.msgReponseStatus) ); 
        //this.robotService.goToComponent("/sign-in");
      }) ;



      this.workstationService.getAll().subscribe(
        (response) => { 
          this.workstationService.listWorkstations = response.body; 
        }
        ,(error) => {
          this.workstationService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          this.store.dispatch( ShowAlert(    this.workstationService.msgReponseStatus) ); 
          //this.workstationService.goToComponent("/sign-in");
        }) ;



  }
  ngAfterViewInit() {
    this.dataSourceRobot.paginator = this.paginatorRobot;
    this.changeDetectorRef.detectChanges(); 
  }
  ngOnDestroy() {
    if (this.dataSourceRobot) {
      this.dataSourceRobot.disconnect();
    }
  }






  onClickAdd():void{
    const dialogRef =  this.openPopupRobot("Add New Robot"); 
    dialogRef.afterClosed().subscribe(result => {
      if (result == null) { return; }
      this.robotService.insert(result).subscribe(
        (response) => { 
          this.robotService.robot = response.body; 
          this.robotService.msgReponseStatus = {title:"Message",datestamp:new Date(),status:ReponseStatus.SUCCESSFUL,message:"success add new workstation"}; 
          this.store.dispatch( ShowAlert(  this.robotService.msgReponseStatus) ); 
          this.robotService.listRobots.push(  this.robotService.robot );
          this.dataSourceRobot.data = this.robotService.listRobots;
        },
        (error:HttpErrorResponse) => {
          if ((error.status === 406 )||(error.status === 403 )) {   this.robotService.msgReponseStatus = error.error; }
          else {
            this.robotService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          }
          this.store.dispatch( ShowAlert(  this.robotService.msgReponseStatus) );  console.log(error.status) ;
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
          this.robotService.robot = response.body; 
          this.robotService.msgReponseStatus = {title:"Message",datestamp:new Date(),status:ReponseStatus.SUCCESSFUL,message:"success add new workstation"}; 
          this.store.dispatch( ShowAlert(  this.robotService.msgReponseStatus) ); 

          const index = this.robotService.listRobots.findIndex(robot => robot.id === this.robotService.robot.id);
          if (index !== -1) {  this.robotService.listRobots[index] =  this.robotService.robot;  } 
        //   this.robotService.listRobots.forEach((w) => { if(w.id === id) { w =   this.robotService.workstation ;} });

          this.dataSourceRobot.data = this.robotService.listRobots;
        },
        (error:HttpErrorResponse) => {
          if ((error.status === 406 )||(error.status === 403 )) {   this.robotService.msgReponseStatus = error.error; }
          else {
            this.robotService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          }
          this.store.dispatch( ShowAlert(  this.robotService.msgReponseStatus) );  console.log(error.status) ;
        }  
        );
    });
  }
  onClickDelete(id: any): void {
    this.openDialogConfirmation('Confirmation', '  Would you want to delete work station equal ' + id + ' ?', '300ms', '500ms',
      () => {
        this.robotService.delete(id).subscribe(
          (response) => {
            this.robotService.msgReponseStatus = response.body;
            this.store.dispatch(ShowAlert(this.robotService.msgReponseStatus));
            this.robotService.listRobots = this.robotService.listRobots.filter(item => item.id !== id);
            this.dataSourceRobot.data = this.robotService.listRobots;
          },
          (error: HttpErrorResponse) => {
            if ((error.status === 406) || (error.status === 403)) { this.robotService.msgReponseStatus = error.error; }
            else {
              this.robotService.msgReponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
            }
            this.store.dispatch(ShowAlert(this.robotService.msgReponseStatus)); console.log(error.status);
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