import { Component, Inject, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { Connection } from 'src/app/core/store/models/Robot/Connection.enum';
import { ModeRobot } from 'src/app/core/store/models/Robot/ModeRobot.enum';
import { OperationStatus } from 'src/app/core/store/models/Robot/OperationStatus.enum';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { StatusRobot } from 'src/app/core/store/models/Robot/StatusRobot.enum';
import { TagState, tagState } from 'src/app/core/store/states/Tag.state';


@Component({
  selector: 'app-add-robot',
  templateUrl: './add-robot.component.html',
  styleUrls: ['./add-robot.component.css']
})
export class AddRobotComponent implements OnInit {
  tagState !: TagState;
  robot!:RobotDto;
  constructor(public workstationService: WorkstationService,public dialogRef: MatDialogRef<AddRobotComponent>,
     @Inject(MAT_DIALOG_DATA) public data: any) { 
       this.tagState  = tagState;
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
        distance:this.data.element.distance,
        codeTag:this.data.element.codeTag};
      
      
      return;} 
      this.robot  = {
      id: "",
      clientid: "",
      username: "",
      password: "",
      createdAt: new Date(),
      name: "",
      statusRobot: StatusRobot.WAITING,
      modeRobot: ModeRobot.MANUAL,
      notice: "",
      connection: Connection.DISCONNECTED,
      operationStatus: OperationStatus.PAUSE,
      levelBattery: 0,
      speed: 0,
      distance:0,
      codeTag:""};
    }
  compareTags(tag1: any, tag2: any): boolean { return tag1 && tag2 ? tag1.id === tag2.id : tag1 === tag2; }
  closepopup() { this.dialogRef.close(null); }
  onSubmitForm(form: NgForm): void { 
   if (!form.invalid) { this.dialogRef.close(this.robot); } 
  }
}

