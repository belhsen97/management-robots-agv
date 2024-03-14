import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { getValueSearchInput } from 'src/app/core/store/Global/App.Selectors';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { AddWorkstationComponent } from '../add-workstation/add-workstation.component';
import { MatDialog } from '@angular/material/dialog';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';
import { WorkstationDto } from 'src/app/core/store/models/Workstation/WorkstationDto.model';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { ShowAlert } from 'src/app/core/store/Global/App.Action';
import { HttpErrorResponse } from '@angular/common/http';
import { TagService } from 'src/app/core/services/tag.service';

@Component({
  selector: 'app-list-workstations',
  templateUrl: './list-workstations.component.html',
  styleUrls: ['./list-workstations.component.css']
})
export class ListWorkstationsComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild("paginatorWorkstation") paginatorWorkstation  !: MatPaginator;
  @ViewChild(MatSort) sortWorkstation   !: MatSort;
  displayedColumnsWorkstation: string[] = ['name', 'enable', 'NRobot', 'NTag', 'action'];
  dataSourceWorkstation !: MatTableDataSource<WorkstationDto>;



  constructor(private store: Store,
    public workstationService: WorkstationService,
    public tagService: TagService,
    private dialog: MatDialog){}





  ngOnInit(): void {
    // this.store.select(getrouterinfo).subscribe(item => {
    //   console.log( item);
    // });
    this.store.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSourceWorkstation == undefined) { return; }
      this.dataSourceWorkstation.filter = value;
      console.log(value);
    });

    this.dataSourceWorkstation = new MatTableDataSource<WorkstationDto>(this.workstationService.listWorkstations);
    this.workstationService.getAll().subscribe(
      (response) => {
        this.workstationService.listWorkstations = response.body;
        // this.workstationService.listWorkstations.forEach((workstation, index) => {workstation.no = 1+index;});
        this.dataSourceWorkstation.data = this.workstationService.listWorkstations;
      }
      , (error) => {
        this.workstationService.msgReponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
        this.store.dispatch(ShowAlert(this.workstationService.msgReponseStatus));
        //this.workstationService.goToComponent("/sign-in");
      });






    this.tagService.getAll().subscribe(
      (response) => {
        this.tagService.listTags = response.body;
      }
      , (error) => {
        this.tagService.msgReponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
        this.store.dispatch(ShowAlert(this.tagService.msgReponseStatus));
        //this.workstationService.goToComponent("/sign-in");
      });




  }
  ngAfterViewInit() {
    this.dataSourceWorkstation.paginator = this.paginatorWorkstation;
    this.dataSourceWorkstation.sort = this.sortWorkstation;
  }
  ngOnDestroy(): void {
    if (this.dataSourceWorkstation) {
      this.dataSourceWorkstation.disconnect();
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





  onClickAddWorkstation(): void {
    const dialogRef = this.openPopupWorkStation(undefined, "Add New WorkStation");
    dialogRef.afterClosed().subscribe(result => {
      if (result == null) { return; }
      // Handle result from the dialog 
      this.workstationService.insert(result).subscribe(
        (response) => {
          this.workstationService.workstation = response.body;
          this.workstationService.msgReponseStatus = { title: "Message", datestamp: new Date(), status: ReponseStatus.SUCCESSFUL, message: "success add new workstation" };
          this.store.dispatch(ShowAlert(this.workstationService.msgReponseStatus));
          // this.workstationService.workstation.no =  this.workstationService.listWorkstations.length+1;
          this.workstationService.listWorkstations.push(this.workstationService.workstation);
          this.dataSourceWorkstation.data = this.workstationService.listWorkstations;
        },
        (error: HttpErrorResponse) => {
          if ((error.status === 406) || (error.status === 403)) { this.workstationService.msgReponseStatus = error.error; }
          else {
            this.workstationService.msgReponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
          }
          this.store.dispatch(ShowAlert(this.workstationService.msgReponseStatus)); console.log(error.status);
        }
      );
    });
  }


  onClickEditWorkstation(element: any): void {
    const dialogRef = this.openPopupWorkStation(element, "Edit WorkStation", true);
    dialogRef.afterClosed().subscribe(result => {
      if (result == null) { return; }
      // Handle result from the dialog 
      this.workstationService.update(element.id, result).subscribe(
        (response) => {
          this.workstationService.workstation = response.body;
          this.workstationService.msgReponseStatus = { title: "Message", datestamp: new Date(), status: ReponseStatus.SUCCESSFUL, message: "success add new workstation" };
          this.store.dispatch(ShowAlert(this.workstationService.msgReponseStatus));

          const index = this.workstationService.listWorkstations.findIndex(element => element.id === this.workstationService.workstation.id);
          if (index !== -1) { this.workstationService.listWorkstations[index] = this.workstationService.workstation; }
          //   this.workstationService.listWorkstations.forEach((w) => { if(w.id === id) { w =   this.workstationService.workstation ;} });

          this.dataSourceWorkstation.data = this.workstationService.listWorkstations;
        },
        (error: HttpErrorResponse) => {
          if ((error.status === 406) || (error.status === 403)) { this.workstationService.msgReponseStatus = error.error; }
          else {
            this.workstationService.msgReponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
          }
          this.store.dispatch(ShowAlert(this.workstationService.msgReponseStatus)); console.log(error.status);
        }
      );
    });
  }



  onClickDeleteWorkstation(id: any): void {
    this.openDialogConfirmation('Confirmation', '  Would you want to delete work station equal ' + id + ' ?', '300ms', '500ms',
      () => {
        this.workstationService.delete(id).subscribe(
          (response) => {
            this.workstationService.msgReponseStatus = response.body;
            this.store.dispatch(ShowAlert(this.workstationService.msgReponseStatus));
            this.workstationService.listWorkstations = this.workstationService.listWorkstations.filter(item => item.id !== id);
            this.dataSourceWorkstation.data = this.workstationService.listWorkstations;
          },
          (error: HttpErrorResponse) => {
            if ((error.status === 406) || (error.status === 403)) { this.workstationService.msgReponseStatus = error.error; }
            else {
              this.workstationService.msgReponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
            }
            this.store.dispatch(ShowAlert(this.workstationService.msgReponseStatus)); console.log(error.status);
          }
        );

      });
  }





  onChangeStatus(id: any, event: any) {
    const selectedStatus = event.value;
    console.log(id, selectedStatus);
  }





















}

