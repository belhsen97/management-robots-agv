import { Component, Inject, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RobotService } from 'src/app/core/services/robot.service';
import { WorkstationService } from 'src/app/core/services/workstation.service';

@Component({
  selector: 'app-add-robot',
  templateUrl: './add-robot.component.html',
  styleUrls: ['./add-robot.component.css']
})
export class AddRobotComponent implements OnInit {
  constructor(public robotService: RobotService,
    public workstationService: WorkstationService,
    public dialogRef: MatDialogRef<AddRobotComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void { if (this.data.element != undefined) { this.robotService.robot = this.data.element; } }
  compareWorkstation(w1: any, w2: any): boolean { return w1 && w2 ? w1.id === w2.id : w1 === w2; }
  closepopup() { this.dialogRef.close(null); }
  onSubmitForm(form: NgForm): void { if (!form.invalid) { this.dialogRef.close(this.robotService.robot); } }
}

