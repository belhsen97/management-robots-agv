import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';
import { NgForm } from '@angular/forms';
import { UserService } from 'src/app/core/store/services/user.service.ts.service';
import { WorkstationService } from 'src/app/core/store/services/workstation.service';
import { ErrorStateMatcher } from '@angular/material/core';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { HttpErrorResponse } from '@angular/common/http';
@Component({
  selector: 'app-add-workstation',
  templateUrl: './add-workstation.component.html',
  styleUrls: ['./add-workstation.component.css']
})
export class AddWorkstationComponent  implements OnInit   {

  constructor(public workstationService: WorkstationService,
    public dialogRef: MatDialogRef<AddWorkstationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
  }
  ngOnInit(): void {
   if ( this.data.element != undefined ){ this.workstationService.workstation =this.data.element;} 
  }
 


  closepopup() {
    this.dialogRef.close(null);
  }


  //onSubmitCallback: Function | undefined;
  onSubmitForm(form: NgForm): void {
    // console.log(form.invalid);
    // console.log(this.workstationService.workstation);
    // if (this.onSubmitCallback) {
    //   this.onSubmitCallback(form);
    // }
    if (!form.invalid) { this.dialogRef.close(this.workstationService.workstation); }
    


  }
}




