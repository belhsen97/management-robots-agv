import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';
import { NgForm } from '@angular/forms';
import { UserService } from 'src/app/core/store/services/user.service.ts.service';
import { WorkstationService } from 'src/app/core/store/services/workstation.service';
import {ErrorStateMatcher} from '@angular/material/core';
@Component({
  selector: 'app-add-workstation',
  templateUrl: './add-workstation.component.html',
  styleUrls: ['./add-workstation.component.css']
})
export class AddWorkstationComponent {
  
  constructor(public workstationService:WorkstationService,  public dialogRef: MatDialogRef<AddWorkstationComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
  }
 

  closepopup() {
    this.dialogRef.close();
  }

  onSubmitForm(form: NgForm):void {
  console.log(form.invalid);
  console.log(this.workstationService.workstation);
  }
}




 