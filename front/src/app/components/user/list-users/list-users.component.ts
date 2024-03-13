import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { Store } from '@ngrx/store';
import { getrouterinfo } from 'src/app/core/store/Router/Router.Seletor';
import { MatTableDataSource } from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator'; 
import { MatSort } from '@angular/material/sort';
import { getValueSearchInput } from 'src/app/core/store/Global/App.Selectors';
import {   UserDto } from 'src/app/core/store/models/User/UserDto.model'; 
import { UserService } from 'src/app/core/services/user.service.ts.service';

import {animate, state, style, transition, trigger} from '@angular/animations';
import { ShowAlert } from 'src/app/core/store/Global/App.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { MsgReponseStatus } from 'src/app/core/store/models/Global/MsgReponseStatus.model';

@Component({
  selector: 'app-list-users',
  templateUrl: './list-users.component.html',
  styleUrls: ['./list-users.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed,void', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class ListUsersComponent implements OnInit , AfterViewInit{


  @ViewChild("paginator") paginator  !: MatPaginator;
  @ViewChild(MatSort) sort  !: MatSort;
  displayedColumns: string[] = ['id', 'createdAt', 'username', 'firstname','lastname', 'enabled','role','action'];
  columnsToDisplayWithExpand = [...this.displayedColumns, 'expand'];
  dataSource:any;

  expandedElement !: UserDto | null;

  constructor( private store: Store ,  public userService : UserService ) {}


  ngOnInit(): void {



    // this.store.select(getrouterinfo).subscribe(item => {
    //   console.log( item);
    // });
    this.store.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSource == undefined ){return ; }
      this.dataSource.filter = value;
      console.log(  value );
    });

    this.dataSource = new MatTableDataSource<UserDto>(this.userService.ListUsers);


    this.userService.getAll().subscribe(
      (response) => { 
        this.userService.ListUsers = response.body; 
        this.userService.ListUsers.forEach((user, index) => {
          user.no = 1+index;
          user.createdAt = this.userService.toDate (user.createdAt.toString() );
      });
        this.dataSource.data =  this.userService.ListUsers ;
      }
      ,(error) => {
        this.userService.msgReponseStatus =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        this.store.dispatch( ShowAlert(  this.userService.msgReponseStatus) ); 
        this.userService.goToComponent("/sign-in");
      }) ;
 
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  onClickEnable(username:any ,enable : boolean ):void{
    console.log(username , enable);  
    this.userService.enableUser(  this.userService.userDto.username ,  enable  ).subscribe(
      (response) => {
        const msg : MsgReponseStatus = response.body;
        this.userService.msgReponseStatus = { title : msg.title, datestamp: new Date() ,status : msg.status , message : msg.message };
       }
      ,(error) => {
        this.userService.msgReponseStatus =  
        { title : "Error", datestamp: new Date(),status : ReponseStatus.ERROR , message : error.message};
      }) ; 
  }

  onClickDelete(id:any ):void{console.log(id);  }

  onChangeRole(username:any , event: any) {
    const selectedRole = event.value;
    console.log(username,typeof selectedRole); 
    this.userService.updateRole(  this.userService.userDto.username ,  selectedRole  ).subscribe(
      (response) => {
        const msg : MsgReponseStatus = response.body;
        this.userService.msgReponseStatus = { title : msg.title, datestamp: new Date() ,status : msg.status , message : msg.message };
       }
      ,(error) => {
        this.userService.msgReponseStatus =  
        { title : "Error", datestamp: new Date(),status : ReponseStatus.ERROR , message : error.message};
      }) ; 
  }


    

  // onClickAdd():void{
  //   this.dataSource.data.push({position: 21, name: 'Hydrogen', weight: 1.0079, symbol: 'H'});
  //   //this.dataSource.data = this.dataSource.data; 
  //   //   this.dataSource.setData(this.dataToDisplay);
  //   this.dataSource.renderRows();
  //   console.log( this.dataSource.data);
  // }

  // removeData() {
  //   this.dataSource.pop();
  //   this.dataSource.renderRows();
  // }
  // onClickEdit(id:any ):void{
  //   console.log(id);  
  // }
}
 