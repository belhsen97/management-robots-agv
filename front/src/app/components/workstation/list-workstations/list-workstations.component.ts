import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { getValueSearchInput } from 'src/app/core/store/selectors/global.Selectors';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { AddWorkstationComponent } from '../add-workstation/add-workstation.component';
import { MatDialog } from '@angular/material/dialog';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { HttpErrorResponse } from '@angular/common/http';
import { TagService } from 'src/app/core/services/tag.service';
import { tagState } from 'src/app/core/store/states/Tag.state';
import { WorkstationDto } from 'src/app/core/store/models/Workstation/WorkstationDto.model';
import {  wsState } from 'src/app/core/store/states/Worstation.state';
import { RobotService } from 'src/app/core/services/robot.service';

@Component({
  selector: 'app-list-workstations',
  templateUrl: './list-workstations.component.html',
  styleUrls: ['./list-workstations.component.css']
})
export class ListWorkstationsComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild("paginatorWorkstation") paginatorWorkstation  !: MatPaginator;
  @ViewChild(MatSort) sortWorkstation   !: MatSort;
  displayedColumnsWorkstation: string[] = ['name', 'enable', 'NRobot', 'NTag', 'action'];
  
  dataSource : MatTableDataSource<WorkstationDto> = new MatTableDataSource<WorkstationDto>();
  //dataSourceWorkstation !: MatTableDataSource<WorkstationDto>;



  constructor(private store: Store,
    public wsService: WorkstationService,
    public tagService: TagService, public robotsService: RobotService,
    private dialog: MatDialog){}





  ngOnInit(): void {
    // this.store.select(getrouterinfo).subscribe(item => {
    //   console.log( item);
    // });
    this.store.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSource == undefined) { return; }
      this.dataSource.filter = value;
      console.log(value);
    });

  
    this.wsService.getAll().subscribe(
      (response) => {
        wsState.listWorkstations = response.body;
        this.dataSource.data = wsState.listWorkstations;
      }
      , (error) => {
        this.wsService.msgResponseStatus  = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
        this.store.dispatch(ShowAlert(this.wsService.msgResponseStatus ));
        //this.workstationService.goToComponent("/sign-in");
      });






    this.tagService.getAll().subscribe(
      (response) => {
        tagState.listTags = response.body;
      }
      , (error) => {
        this.wsService.msgResponseStatus  = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
        this.store.dispatch(ShowAlert(this.wsService.msgResponseStatus ));
        //this.workstationService.goToComponent("/sign-in");
      });

  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginatorWorkstation;
    this.dataSource.sort = this.sortWorkstation;
  }
  ngOnDestroy(): void {
    if (this.dataSource) {
      this.dataSource.disconnect();
    }
  }





  openPopupWorkStation(element: any, title: any, isedit = false) {
    const dialogRef = this.dialog.open(AddWorkstationComponent, {
      width: '40%',
      data: {
        element: element,
        title: title,
        isedit: isedit
      }
    });
    return dialogRef;
    // dialogRef.componentInstance.onSubmitCallback = (form: any) => {
    //   console.log(form.invalid);
    //   console.log(this.workstationService.workstation);
    // };
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





  onClickAddWS(): void {
    const dialogRef = this.openPopupWorkStation(undefined, "Add New WorkStation");
    dialogRef.afterClosed().subscribe(result => {
      if (result == null) { return; }
      // Handle result from the dialog 
      this.wsService.insert(result).subscribe(
        (response) => {
          wsState.workstation = response.body;
          this.wsService.msgResponseStatus  = { title: "Message", datestamp: new Date(), status: ReponseStatus.SUCCESSFUL, message: "success add new workstation" };
          this.store.dispatch(ShowAlert(this.wsService.msgResponseStatus ));
          // this.workstationService.workstation.no =  this.workstationService.listWorkstations.length+1;
          wsState.listWorkstations.push(wsState.workstation);
          this.dataSource.data = wsState.listWorkstations;
        },
        (error: HttpErrorResponse) => {
          if ((error.status === 406) || (error.status === 403)) { this.wsService.msgResponseStatus  = error.error; }
          else {
            this.wsService.msgResponseStatus  = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
          }
          this.store.dispatch(ShowAlert(this.wsService.msgResponseStatus )); console.log(error.status);
        }
      );
    });
  }


  onClickEditWS(element: any): void {
    const dialogRef = this.openPopupWorkStation(element, "Edit WorkStation", true);
    dialogRef.afterClosed().subscribe(result => {
      if (result == null) { return; }
      // Handle result from the dialog 
      this.wsService.update(element.id, result).subscribe(
        (response) => {
          wsState.workstation = response.body;
          this.wsService.msgResponseStatus  = { title: "Message", datestamp: new Date(), status: ReponseStatus.SUCCESSFUL, message: "success add new workstation" };
          this.store.dispatch(ShowAlert(this.wsService.msgResponseStatus ));

          const index = wsState.listWorkstations.findIndex(element => element.id === wsState.workstation.id);
          if (index !== -1) { wsState.listWorkstations[index] = wsState.workstation; }
          //   this.workstationService.listWorkstations.forEach((w) => { if(w.id === id) { w =   this.workstationService.workstation ;} });

          this.dataSource.data = wsState.listWorkstations;
        },
        (error: HttpErrorResponse) => {
          if ((error.status === 406) || (error.status === 403)) { this.wsService.msgResponseStatus  = error.error; }
          else {
            this.wsService.msgResponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
          }
          this.store.dispatch(ShowAlert(this.wsService.msgResponseStatus )); console.log(error.status);
        }
      );
    });
  }



  onClickDeleteWS(id: any): void {
    this.openDialogConfirmation('Confirmation', '  Would you want to delete work station equal ' + id + ' ?', '300ms', '500ms',
      () => {
        this.wsService.delete(id).subscribe(
          (response) => {
            this.wsService.msgResponseStatus = response.body;
            this.store.dispatch(ShowAlert(this.wsService.msgResponseStatus ));
            wsState.listWorkstations = wsState.listWorkstations.filter(item => item.id !== id);
            this.dataSource.data = wsState.listWorkstations;
          },
          (error: HttpErrorResponse) => {
            if ((error.status === 406) || (error.status === 403)) { this.wsService.msgResponseStatus = error.error; }
            else {
              this.wsService.msgResponseStatus  = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
            }
            this.store.dispatch(ShowAlert(this.wsService.msgResponseStatus )); console.log(error.status);
          }
        );

      });
  }





  onChangeStatus(id: any, event: any) {
    const selectedStatus = event.value;
    console.log(id, selectedStatus);
  }





















}

