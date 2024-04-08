import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { Store } from '@ngrx/store';
import { getrouterinfo } from 'src/app/core/store/selectors/Router.Seletor';
import { MatTableDataSource } from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator'; 
import { MatSort } from '@angular/material/sort';
import { getValueSearchInput } from 'src/app/core/store/selectors/global.Selectors';
import {   UserDto } from 'src/app/core/store/models/User/UserDto.model'; 
import { UserService } from 'src/app/core/services/user.service.ts.service';

import {animate, state, style, transition, trigger} from '@angular/animations';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { MsgResponseStatus } from 'src/app/core/store/models/Global/MsgResponseStatus.model';
import { UserState, userState } from 'src/app/core/store/states/User.state';
import { globalState } from 'src/app/core/store/states/Global.state';

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
  userState !: UserState;
  constructor( private store: Store ,  public userService : UserService ) {this.userState = userState;}


  ngOnInit(): void {



    // this.store.select(getrouterinfo).subscribe(item => {
    //   console.log( item);
    // });
    this.store.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSource == undefined ){return ; }
      this.dataSource.filter = value;
      console.log(  value );
    });

    this.dataSource = new MatTableDataSource<UserDto>(userState.ListUsers);


    this.userService.getAll().subscribe(
      (response) => { 
        userState.ListUsers = response.body; 
        userState.ListUsers.forEach((user, index) => {
          user.no = 1+index;
          user.createdAt = this.userService.toDate (user.createdAt.toString() );
          if ( user.photo == null )  { user.photo = userState.defaultPhotoUser;}
      });
        this.dataSource.data =  userState.ListUsers ;
      }
      ,(error) => {
        this.userService.msgResponseStatus  =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        this.store.dispatch( ShowAlert(this.userService.msgResponseStatus ) ); 
        this.userService.goToComponent("/sign-in");
      }) ;
 
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  onClickEnable(username:any ,enable : boolean ):void{
    console.log(username, enable)
    this.userService.enableUser( username ,  enable  ).subscribe(
      (response) => {
        const msg : MsgResponseStatus = response.body;
        this.userService.msgResponseStatus  = { title : msg.title, datestamp: new Date() ,status : msg.status , message : msg.message };
        this.store.dispatch( ShowAlert(this.userService.msgResponseStatus ) ); 
   
        const index = userState.ListUsers.findIndex(u => u.username === username);
        if (index !== -1) {  userState.ListUsers[index].enabled =  enable;  } 
        this.dataSource.data = userState.ListUsers;

      }
      ,(error) => {
        this.userService.msgResponseStatus  =  
        { title : "Error", datestamp: new Date(),status : ReponseStatus.ERROR , message : error.message};
        this.store.dispatch( ShowAlert( this.userService.msgResponseStatus ) ); 
      }) ; 
  }

  onClickDelete(id:any ):void{console.log(id);  }

  onChangeRole(username:any , event: any) {
    const selectedRole = event.value;
    console.log(username,typeof selectedRole); 
    this.userService.updateRole( username ,  selectedRole  ).subscribe(
      (response) => {
        const msg : MsgResponseStatus = response.body;
        this.userService.msgResponseStatus  = { title : msg.title, datestamp: new Date() ,status : msg.status , message : msg.message };
        this.store.dispatch( ShowAlert( this.userService.msgResponseStatus ) ); 

        console.log(typeof (event));
        console.log(typeof (selectedRole));
        const index = userState.ListUsers.findIndex(u => u.username === username);
        if (index !== -1) {  userState.ListUsers[index].role =  event.value;  } 
        this.dataSource.data = userState.ListUsers;

      }
      ,(error) => {
        this.userService.msgResponseStatus  =  
        { title : "Error", datestamp: new Date(),status : ReponseStatus.ERROR , message : error.message};
        this.store.dispatch( ShowAlert(this.userService.msgResponseStatus ) ); 
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
 