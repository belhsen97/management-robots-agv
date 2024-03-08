import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { getValueSearchInput } from 'src/app/core/store/Global/App.Selectors'; 
import { WorkstationService } from 'src/app/core/store/services/workstation.service';
import { AddWorkstationComponent } from '../add-workstation/add-workstation.component';
import { MatDialog } from '@angular/material/dialog';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';
import { WorkstationDto } from 'src/app/core/store/models/Workstation/WorkstationDto.model';
import { Tag } from 'src/app/core/store/models/Workstation/Tag.model';
import { AddTagComponent } from '../add-tag/add-tag.component';

@Component({
  selector: 'app-list-workstations',
  templateUrl: './list-workstations.component.html',
  styleUrls: ['./list-workstations.component.css']
})
export class ListWorkstationsComponent implements OnInit , AfterViewInit, OnDestroy {
  @ViewChild("paginatorWorkstation") paginatorWorkstation  !: MatPaginator;
  @ViewChild(MatSort) sortWorkstation   !: MatSort;
  @ViewChild("paginatorTag") paginatorTag  !: MatPaginator;
  @ViewChild(MatSort) sortTag   !: MatSort;
  displayedColumnsWorkstation: string[] = ['id', 'name', 'enable','action'];
  displayedColumnsTag : string[] = ['id', 'code', 'description','action'];
  dataSourceWorkstation:any;
  dataSourceTag :any;



  constructor( private store: Store ,  public workstationService : WorkstationService,private dialog: MatDialog ) {}





  ngOnInit(): void {
    // this.store.select(getrouterinfo).subscribe(item => {
    //   console.log( item);
    // });
    this.store.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSourceWorkstation == undefined ){return ; }
      this.dataSourceWorkstation.filter = value;
      console.log(value);
    });


    this.dataSourceWorkstation = new MatTableDataSource<WorkstationDto>(this.workstationService.listWorkstations);
    this.dataSourceTag= new MatTableDataSource<Tag>(this.workstationService.listTags);
 
  }
  ngAfterViewInit() {
    this.dataSourceWorkstation.paginator = this.paginatorWorkstation;
    this.dataSourceWorkstation.sort = this.sortWorkstation;
    this.dataSourceTag.paginator = this.paginatorTag;
    this.dataSourceTag.sort = this.sortTag;
  }
  ngOnDestroy(): void {
    if (this.dataSourceWorkstation) {
      this.dataSourceWorkstation.disconnect();
    }
    if (this.dataSourceTag) {
      this.dataSourceTag.disconnect();
    }
  }





  openPopupWorkStation(id: any, title: any, isedit = false) {
    this.dialog.open(AddWorkstationComponent, {
      width: '40%',
      data: {
        id: id,
        title: title,
        isedit: isedit
      }
    })
  }
  openPopupTag(id: any, title: any, isedit = false) {
    this.dialog.open(AddTagComponent, {
      width: '40%',
      data: {
        id: id,
        title: title,
        isedit: isedit
      }
    })
  }
   //Msg box
   openDialogConfirmation(title:string ,message:string, enterAnimationDuration: string, 
    exitAnimationDuration: string ,  callback: () => void): void {
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
 


 

  onClickAddWorkstation():void{
    this.openPopupWorkStation(0, "Add New WorkStation"); 
  }
  onClickAddTag():void{
    this.openPopupTag(0, "Add New Tag"); 
  }
  onClickEditWorkstation(id:any ):void{
    this.openPopupWorkStation(id,"Edit WorkStation",true);
  }
  onClickEditTag(id:any ):void{
    this.openPopupTag(id,"Edit Tag",true);
  }
  onClickDeleteWorkstation(id:any ):void{
    this.openDialogConfirmation('Confirmation', '  Would you want to delete work station equal '+id+' ?','300ms', '500ms',
    () => { 

    });
  }
  onClickDeleteTag(id:any ):void{
    this.openDialogConfirmation('Confirmation', '  Would you want to delete Tag equal '+id+' ?','300ms', '500ms',
    () => { 

    });
  }


  
  // onClickDeleteAll() { 
  //   this.openDialogConfirmation('Confirmation', '  Would you want to delete All work station equal  ?','300ms', '500ms',
  //   () => { 
  //   });
  // }

   
  onChangeStatus(id:any,event: any) {
    const selectedStatus = event.value;
    console.log(id,selectedStatus);}
  onClickActivate(id:any,enable : boolean){}
}
 
 