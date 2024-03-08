import { Component, Inject } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { WorkstationService } from 'src/app/core/store/services/workstation.service';

@Component({
  selector: 'app-add-tag',
  templateUrl: './add-tag.component.html',
  styleUrls: ['./add-tag.component.css']
})
export class AddTagComponent {

  constructor(public workstationService:WorkstationService,  public dialogRef: MatDialogRef<AddTagComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
  }
 

  closepopup() {
    this.dialogRef.close();
  }

  onSubmitForm(form: NgForm):void {
  console.log(form.invalid);
  console.log(this.workstationService.tag);
  }
}
