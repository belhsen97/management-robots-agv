import { Component, Inject } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RobotService } from 'src/app/core/services/robot.service';

@Component({
  selector: 'app-add-robot',
  templateUrl: './add-robot.component.html',
  styleUrls: ['./add-robot.component.css']
})
export class AddRobotComponent {
  constructor(public robotService:RobotService,  public dialogRef: MatDialogRef<AddRobotComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
  }
 

  closepopup() {
    this.dialogRef.close();
  }

 


  onSubmitForm(form: NgForm):void {
  console.log(form.invalid);
  console.log(this.robotService.robot);
  }
}
