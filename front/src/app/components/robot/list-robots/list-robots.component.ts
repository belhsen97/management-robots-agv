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
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';
import { wsState } from 'src/app/core/store/states/Worstation.state';
import { getListRobot } from 'src/app/core/store/selectors/Robot.Selector';
import { addRobot, deleteRobot, updateRobot } from 'src/app/core/store/actions/Robot.Action';

@Component({
  selector: 'app-list-robots',
  templateUrl: './list-robots.component.html',
  styleUrls: ['./list-robots.component.css']
})
export class ListRobotsComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild("paginator") paginatorRobot!: MatPaginator;
  obs!: Observable<any>;
  dataSource !: MatTableDataSource<RobotDto>;

  constructor(private store: Store,
    private workstationService: WorkstationService,
    private changeDetectorRef: ChangeDetectorRef,
    public robotService: RobotService,
    private storeRobot: Store<RobotState>,
    private dialog: MatDialog) { }

  ngOnInit() {
    this.dataSource = new MatTableDataSource<RobotDto>();

    this.store.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSource == undefined) { return; }
      this.dataSource.filter = value;
    });

    this.obs = this.dataSource.connect();



    this.storeRobot.select(getListRobot).subscribe(item => {
      this.dataSource.data = item;
    }
    );

    this.workstationService.getAll().subscribe(
      (response) => {
        wsState.listWorkstations = response.body;
      }
      , (error) => {
        this.robotService.msgResponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
        this.store.dispatch(ShowAlert(this.robotService.msgResponseStatus));
      });
  }


  ngAfterViewInit() {
      this.dataSource.paginator = this.paginatorRobot;
      this.changeDetectorRef.detectChanges();
    }
  ngOnDestroy() { if (this.dataSource) { this.dataSource.disconnect();} }

  onClickAdd(): void {
    const dialogRef = this.openPopupRobot("Add New Robot");
    dialogRef.afterClosed().subscribe(result => {
      if (result == null) { return; }
      this.storeRobot.dispatch(addRobot({ robotinput: result }));
    });
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
      data: { element: element, title: title, isedit: isedit }
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