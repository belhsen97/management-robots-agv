import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { getValueSearchInput } from 'src/app/core/store/Global/App.Selectors'; 
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { MatDialog } from '@angular/material/dialog';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';
import { TagDto } from 'src/app/core/store/models/Tag/TagDto.model';
import { AddTagComponent } from '../add-tag/add-tag.component';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { ShowAlert } from 'src/app/core/store/Global/App.Action';
import { HttpErrorResponse } from '@angular/common/http';
import { TagService } from 'src/app/core/services/tag.service';

@Component({
  selector: 'app-list-tags',
  templateUrl: './list-tags.component.html',
  styleUrls: ['./list-tags.component.css']
})
export class ListTagsComponent implements OnInit , AfterViewInit, OnDestroy {
  @ViewChild("paginatorTag") paginatorTag  !: MatPaginator;
  @ViewChild(MatSort) sortTag   !: MatSort;
  displayedColumnsTag : string[] = [ 'code', 'workstation','action']; 



  constructor( private store: Store,
    public workstationService:WorkstationService,
    public tagService:TagService,
    private dialog: MatDialog ) {}





  ngOnInit(): void {
    // this.store.select(getrouterinfo).subscribe(item => {
    //   console.log( item);
    // });
    this.store.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.tagService.dataSource == undefined ){return ; }
      this.tagService.dataSource.filter = value;
      console.log(value);
    });





    this.workstationService.getAll().subscribe(
      (response) => { 
        this.workstationService.listWorkstations = response.body; 
      }
      ,(error) => {
        this.workstationService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        this.store.dispatch( ShowAlert(    this.workstationService.msgReponseStatus) ); 
        //this.workstationService.goToComponent("/sign-in");
      }) ;

    this.tagService.dataSource= new MatTableDataSource<TagDto>(this.tagService.listTags);
    this.tagService.getAll().subscribe(
      (response) => { 
        this.tagService.listTags = response.body; 
        this.tagService.dataSource.data =    this.tagService.listTags ;
      }
      ,(error) => {
        this.tagService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        this.store.dispatch( ShowAlert(    this.tagService.msgReponseStatus) ); 
        //this.workstationService.goToComponent("/sign-in");
      }) ;
 


      this.tagService.getAll().subscribe(
      (response) => { 
        this.tagService.listTags = response.body; 
        this.tagService.dataSource.data =    this.tagService.listTags ;
      }
      ,(error) => {
        this.tagService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        this.store.dispatch( ShowAlert(    this.tagService.msgReponseStatus) ); 
        //this.workstationService.goToComponent("/sign-in");
      }) ;
  }
  ngAfterViewInit() {
    this.tagService.dataSource.paginator = this.paginatorTag;
    this.tagService.dataSource.sort = this.sortTag;
  }
  ngOnDestroy(): void {
    if (this.tagService.dataSource) {
      this.tagService.dataSource.disconnect();
    }
  }








  openPopupTag(element: any, title: any, isedit = false) {
    const dialogRef = this.dialog.open(AddTagComponent, {
      width: '40%',
      data: {
        element: element,
        title: title,
        isedit: isedit
      }
    })
    return dialogRef;
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
 





   
  onChangeStatus(id:any,event: any) {
    const selectedStatus = event.value;
    console.log(id,selectedStatus);}


















    onClickAddTag():void{
      const dialogRef =   this.openPopupTag(undefined, "Add New Tag");
      dialogRef.afterClosed().subscribe(result => {if (result==null){ return;}
      this.tagService.insert(result).subscribe(
        (response) => { 
          this.tagService.tag = response.body; 
          this.tagService.msgReponseStatus = {title:"Message",datestamp:new Date(),status:ReponseStatus.SUCCESSFUL,message:"success add new workstation"}; 
          this.store.dispatch( ShowAlert(  this.tagService.msgReponseStatus) ); 
          this.tagService.listTags.push(  this.tagService.tag );
          this.tagService.dataSource.data = this.tagService.listTags;
        },
        (error:HttpErrorResponse) => {
          if ((error.status === 406 )||(error.status === 403 )) {   this.tagService.msgReponseStatus = error.error; }
          else {
            this.tagService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          }
          this.store.dispatch( ShowAlert(  this.tagService.msgReponseStatus) );  console.log(error.status) ;
        }  
        );
    } );
    }
    onClickEditTag(tag :any ):void{
      const dialogRef =  this.openPopupTag(tag,"Edit Tag",true);
      dialogRef.afterClosed().subscribe(result => {if (result==null){ return;}
      this.tagService.update(tag.id,result).subscribe(
        (response) => { 
          this.tagService.tag = response.body; 
          this.tagService.msgReponseStatus = {title:"Message",datestamp:new Date(),status:ReponseStatus.SUCCESSFUL,message:"success add new workstation"}; 
          this.store.dispatch( ShowAlert(  this.tagService.msgReponseStatus) ); 

          const index = this.tagService.listTags.findIndex(tag => tag.id === this.tagService.tag.id);
          if (index !== -1) {  this.tagService.listTags[index] =  this.tagService.tag;  } 
        //   this.tagService.listTags.forEach((w) => { if(w.id === id) { w =   this.tagService.workstation ;} });

          this.tagService.dataSource.data = this.tagService.listTags;
        },
        (error:HttpErrorResponse) => {
          if ((error.status === 406 )||(error.status === 403 )) {   this.tagService.msgReponseStatus = error.error; }
          else {
            this.tagService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          }
          this.store.dispatch( ShowAlert(  this.tagService.msgReponseStatus) );  console.log(error.status) ;
        }  
        );
    } );
    }




    onClickDeleteTag(id:any ):void{
      this.openDialogConfirmation('Confirmation', '  Would you want to delete Tag equal '+id+' ?','300ms', '500ms',
      () => { 
        this.tagService.delete(id).subscribe(
          (response) => { 
            this.tagService.msgReponseStatus = response.body; 
            this.store.dispatch( ShowAlert(  this.tagService.msgReponseStatus) ); 
            this.tagService.listTags = this.tagService.listTags.filter(item => item.id !== id);
            this.tagService.dataSource.data = this.tagService.listTags;
          },
          (error:HttpErrorResponse) => {
            if ((error.status === 406 )||(error.status === 403 )) {   this.tagService.msgReponseStatus = error.error; }
            else {
              this.tagService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
            }
            this.store.dispatch( ShowAlert(  this.tagService.msgReponseStatus) );  console.log(error.status) ;
          }  
          );
      });
    }
  

}
 