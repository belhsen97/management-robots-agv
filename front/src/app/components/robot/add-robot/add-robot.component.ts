import { Component, Inject, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { Connection } from 'src/app/core/store/models/Robot/Connection.enum';
import { ModeRobot } from 'src/app/core/store/models/Robot/ModeRobot.enum';
import { OperationStatus } from 'src/app/core/store/models/Robot/OperationStatus.enum';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { StatusRobot } from 'src/app/core/store/models/Robot/StatusRobot.enum';
import { WorkstationState, wsState } from 'src/app/core/store/states/Worstation.state';


@Component({
  selector: 'app-add-robot',
  templateUrl: './add-robot.component.html',
  styleUrls: ['./add-robot.component.css']
})
export class AddRobotComponent implements OnInit {
  robot!:RobotDto;
  wsState !: WorkstationState;
  constructor(public workstationService: WorkstationService,public dialogRef: MatDialogRef<AddRobotComponent>,
     @Inject(MAT_DIALOG_DATA) public data: any) { 
       this.wsState  = wsState;
    }
  ngOnInit(): void {
     if (this.data.element != undefined) {
      this.robot  = {
        id: this.data.element.id,
        clientid: this.data.element.clientid,
        username: this.data.element.username,
        password: this.data.element.password,
        createdAt: this.data.element.createdAt,
        name: this.data.element.name,
        statusRobot: this.data.element.statusRobot,
        modeRobot: this.data.element.modeRobot,
        notice: this.data.element.notice,
        connection: this.data.element.connection,
        operationStatus: this.data.element.operationStatus,
        levelBattery: this.data.element.levelBattery,
        speed: this.data.element.speed,
        workstation: this.data.element.workstation};
      
      
      return;} 
      this.robot  = {
      id: "",
      clientid: "",
      username: "",
      password: "",
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
  onSubmitForm(form: NgForm): void { if (!form.invalid) { this.dialogRef.close(this.robot); } }
}

