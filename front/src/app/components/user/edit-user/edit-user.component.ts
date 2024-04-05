import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Store } from '@ngrx/store';
import { AttachementDto } from 'src/app/core/store/models/User/AttachementDto.model';
import { Gender } from 'src/app/core/store/models/User/Gender.enum';
import { MsgResponseStatus } from 'src/app/core/store/models/Global/MsgResponseStatus.model';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { Role } from 'src/app/core/store/models/User/Role.enum';
import { UserService } from 'src/app/core/services/user.service.ts.service';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';
import { MatDialog } from '@angular/material/dialog';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { UserState, userState } from 'src/app/core/store/states/User.state';
import { globalState } from 'src/app/core/store/states/Global.state';




@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {
  userState !: UserState;
  constructor(public userService : UserService  , private store: Store , public dialog: MatDialog){this.userState = userState;}
   ngOnInit(): void { 
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
 




//Personal Information  upload image profile
  stateMsgBoxUploadImg : boolean = false;
  onClickOnUploadPersonalInformation():void { 
    this.stateMsgBoxUploadImg = true;
  }
  onYesNoEventUploadImgPersonalInformation($event:any):void {this.stateMsgBoxUploadImg = $event ;} 
uploadImageProfile($event:File):void {
  this.userService.updatePhotoProfile(  this.userState.userDto.username , $event ).subscribe(
    (response) => {
      const photo_Profile : AttachementDto = response.body;
      this.userState.userDto.photo =  photo_Profile;
      this.userService.setUserDto(this.userState.userDto);
     }
    ,(error) => { 
      this.userService.msgResponseStatus  =  
      { title : "Error", datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message};
      this.store.dispatch( ShowAlert(this.userService.msgResponseStatus ) ); 
    }) ;
}


//Personal Information  upload role
stateMsgBoxUploadRolePersonalInformation : boolean = false;
onClickOnUploadRolePersonalInformation():void {
    this.stateMsgBoxUploadRolePersonalInformation = true;

}
onYesNoEventUploadRolePersonalInformation($event:any):void {this.stateMsgBoxUploadRolePersonalInformation = $event ;} 
updateRoleProfile($event:Role):void {
  console.log($event);
   this.userService.updateRoleWithPermission(  this.userState.userDto.username ,  this.userState.userDto.role , $event ).subscribe(
    (response) => {
      const msg : MsgResponseStatus = response.body;
      this.userService.msgResponseStatus =  
      { title : msg.title, datestamp: new Date() ,status : msg.status , message : msg.message };

     }
    ,(error) => {
      this.userService.msgResponseStatus =  
      { title : "Error", datestamp: new Date(),status : ReponseStatus.ERROR , message : error.message};
    }) ; 
}





//Personal Information
onCheckRadioGenderOption(gender:Gender):void {this.userState.userDto.gender = gender ;}
onClickOnSubmitPersonalInformation(form: NgForm):void { 
   if(  !form.invalid ){
    this.openDialogConfirmation('Confirmation', '  Would you want to change you Personal Information ?','300ms', '500ms',
    () => { 

      this.userService.update( this.userState.userDto .id,this.userState.userDto) .subscribe(
        (response) => {     
          this.userState.userDto = response.body;
          this.userService.setUserDto(this.userState.userDto);
          this.userService.msgResponseStatus  =    { title : "Success",   datestamp: new Date() ,status : ReponseStatus.SUCCESSFUL , message : "updated !"}
          this.store.dispatch( ShowAlert( this.userService.msgResponseStatus ) ); 
        }
        ,(error) => {
          this.userService.msgResponseStatus  =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          this.store.dispatch( ShowAlert(this.userService.msgResponseStatus ) ); 
        }) ;

    });
   }  
  }
 





//Change Password
isNotSamePassword : boolean = false;
public currentPassword! :string ;
public newPassword! : string;
public valodatePassword! : string;
onClickSubmitUpdatePassword(form: NgForm):void {
  if(  !form.invalid ){
    this.openDialogConfirmation('Confirmation', 'Do you want to change you Password ?','300ms', '500ms',
    () => { 

    this.userService.updatePassword( this.userState.userDto.username,this.currentPassword,this.newPassword) .subscribe(
      (response) => {
        this.userService.msgResponseStatus  = response.body;
      }
      ,(error) => {
        this.userService.msgResponseStatus  =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
          this.store.dispatch( ShowAlert( this.userService.msgResponseStatus ) ); 
      }) ;

    });



   }  
}

validatorPassword():void{
  if (this.newPassword != this.valodatePassword){this.isNotSamePassword = true;}else {this.isNotSamePassword = false;}
}











}
