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
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { ShowAlert } from 'src/app/core/store/Global/App.Action';
import { HttpErrorResponse } from '@angular/common/http';

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
  displayedColumnsWorkstation: string[] = ['no', 'name', 'enable','action'];
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
    this.workstationService.getAll().subscribe(
      (response) => { 
        this.workstationService.listWorkstations = response.body; 
        this.workstationService.listWorkstations.forEach((workstation, index) => {workstation.no = 1+index;});
        this.dataSourceWorkstation.data =    this.workstationService.listWorkstations ;
      }
      ,(error) => {
        this.workstationService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        this.store.dispatch( ShowAlert(    this.workstationService.msgReponseStatus) ); 
        //this.workstationService.goToComponent("/sign-in");
      }) ;





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





  openPopupWorkStation(element: any, title: any, isedit = false) {
   const dialogRef =  this.dialog.open(AddWorkstationComponent, {
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
    const dialogRef = this.openPopupWorkStation(undefined, "Add New WorkStation");
    dialogRef.afterClosed().subscribe(result => {if (result==null){ return;}
      // Handle result from the dialog 
      this.workstationService.insert(result).subscribe(
        (response) => { 
          this.workstationService.workstation = response.body; 
          this.workstationService.msgReponseStatus = {title:"Message",datestamp:new Date(),status:ReponseStatus.SUCCESSFUL,message:"success add new workstation"}; 
          this.store.dispatch( ShowAlert(  this.workstationService.msgReponseStatus) ); 
          this.workstationService.workstation.no =  this.workstationService.listWorkstations.length+1;
          this.workstationService.listWorkstations.push(  this.workstationService.workstation );
          this.dataSourceWorkstation.data = this.workstationService.listWorkstations;
        },
        (error:HttpErrorResponse) => {
          if ((error.status === 406 )||(error.status === 403 )) {   this.workstationService.msgReponseStatus = error.error; }
          else {
            this.workstationService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          }
          this.store.dispatch( ShowAlert(  this.workstationService.msgReponseStatus) );  console.log(error.status) ;
        }  
        );
    });
  }


  onClickEditWorkstation(element:any ):void{
    const dialogRef = this.openPopupWorkStation(element,"Edit WorkStation",true);
    dialogRef.afterClosed().subscribe(result => {if (result==null){ return;}
      // Handle result from the dialog 
      this.workstationService.update(element.id,result).subscribe(
        (response) => { 
          this.workstationService.workstation = response.body; 
          this.workstationService.msgReponseStatus = {title:"Message",datestamp:new Date(),status:ReponseStatus.SUCCESSFUL,message:"success add new workstation"}; 
          this.store.dispatch( ShowAlert(  this.workstationService.msgReponseStatus) ); 

          const index = this.workstationService.listWorkstations.findIndex(element => element.id === this.workstationService.workstation.id);
          if (index !== -1) {  this.workstationService.listWorkstations[index] =  this.workstationService.workstation;  } 
        //   this.workstationService.listWorkstations.forEach((w) => { if(w.id === id) { w =   this.workstationService.workstation ;} });

          this.dataSourceWorkstation.data = this.workstationService.listWorkstations;
        },
        (error:HttpErrorResponse) => {
          if ((error.status === 406 )||(error.status === 403 )) {   this.workstationService.msgReponseStatus = error.error; }
          else {
            this.workstationService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          }
          this.store.dispatch( ShowAlert(  this.workstationService.msgReponseStatus) );  console.log(error.status) ;
        }  
        );
    });
  }



  onClickDeleteWorkstation(id:any ):void{
    this.openDialogConfirmation('Confirmation', '  Would you want to delete work station equal '+id+' ?','300ms', '500ms',
    () => { 
     console.log(id)
      this.workstationService.delete(id).subscribe(
        (response) => { 
          this.workstationService.msgReponseStatus = response.body; 
          this.store.dispatch( ShowAlert(  this.workstationService.msgReponseStatus) ); 
          this.workstationService.listWorkstations = this.workstationService.listWorkstations.filter(item => item.id !== id);
          this.dataSourceWorkstation.data = this.workstationService.listWorkstations;
        },
        (error:HttpErrorResponse) => {
          if ((error.status === 406 )||(error.status === 403 )) {   this.workstationService.msgReponseStatus = error.error; }
          else {
            this.workstationService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          }
          this.store.dispatch( ShowAlert(  this.workstationService.msgReponseStatus) );  console.log(error.status) ;
        }  
        );

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


















    onClickAddTag():void{
      this.openPopupTag(0, "Add New Tag"); 
    }
    onClickEditTag(id :any ):void{
      
        this.openPopupTag(id,"Edit Tag",true);
    
  
    }
    onClickDeleteTag(id:any ):void{
      this.openDialogConfirmation('Confirmation', '  Would you want to delete Tag equal '+id+' ?','300ms', '500ms',
      () => { 
  
      });
    }
  

}
 
 