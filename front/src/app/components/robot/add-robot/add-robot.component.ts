import { Component, Inject, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { Connection } from 'src/app/core/store/models/Robot/Connection.enum';
import { ModeRobot } from 'src/app/core/store/models/Robot/ModeRobot.enum';
import { OperationStatus } from 'src/app/core/store/models/Robot/OperationStatus.enum';
import { StatusRobot } from 'src/app/core/store/models/Robot/StatusRobot.enum';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';
import { WorkstationState, wsState } from 'src/app/core/store/states/Worstation.state';


@Component({
  selector: 'app-add-robot',
  templateUrl: './add-robot.component.html',
  styleUrls: ['./add-robot.component.css']
})
export class AddRobotComponent implements OnInit {
  robotState !: RobotState;
  wsState !: WorkstationState;
  constructor(public workstationService: WorkstationService,public dialogRef: MatDialogRef<AddRobotComponent>,
     @Inject(MAT_DIALOG_DATA) public data: any) { 
      this.robotState  = robotState; this.wsState  = wsState;
    }
  ngOnInit(): void {

     if (this.data.element != undefined) { robotState.robot = this.data.element; return ;} 
     robotState.robot  = {
      id: "",
      createdAt: new Date(),
      name: "",
      statusRobot: StatusRobot.RUNNING,
      modeRobot: ModeRobot.MANUAL,
      notice: "",
      connection: Connection.CONNECTED,
      operationStatus: OperationStatus.PAUSE,
      levelBattery: 0,speed: 0,workstation: this.wsState.workstation};
    }
  compareWorkstation(w1: any, w2: any): boolean { return w1 && w2 ? w1.id === w2.id : w1 === w2; }
  closepopup() { this.dialogRef.close(null); }
  onSubmitForm(form: NgForm): void { if (!form.invalid) { this.dialogRef.close(robotState.robot); } }
}

