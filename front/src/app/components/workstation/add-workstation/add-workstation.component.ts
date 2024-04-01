import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NgForm } from '@angular/forms';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { TagService } from 'src/app/core/services/tag.service';
import { RobotService } from 'src/app/core/services/robot.service';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';
import { TagState, tagState } from 'src/app/core/store/states/Tag.state';
import { WorkstationState, wsState } from 'src/app/core/store/states/Worstation.state';
@Component({
  selector: 'app-add-workstation',
  templateUrl: './add-workstation.component.html',
  styleUrls: ['./add-workstation.component.css']
})
export class AddWorkstationComponent  implements OnInit   {
  tagState !: TagState;
  robotState !: RobotState;
  wsState !: WorkstationState;
  constructor(public workstationService: WorkstationService,
     public tagService:TagService,
     public robotService:RobotService,
    public dialogRef: MatDialogRef<AddWorkstationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {  
         this.robotState  = robotState;
         this.tagState  = tagState;
         this.wsState  = wsState;
        }
  ngOnInit(): void {if ( this.data.element != undefined ){ this.wsState.workstation =this.data.element; return;}
  this.wsState.workstation={id:"",name: "",enable: true,tags: [], robots: []};
}
 

  compareTags(tag1: any, tag2: any): boolean {  return tag1 && tag2 ? tag1.id === tag2.id : tag1 === tag2; }
  compareRobots(r1: any, r2: any): boolean {  return r1 && r2 ? r1.id === r2.id : r1 === r2; }


  closepopup() {this.dialogRef.close(null);}


  //onSubmitCallback: Function | undefined;
  onSubmitForm(form: NgForm): void {
    // console.log(form.invalid);
    // console.log(this.workstationService.workstation);
    // if (this.onSubmitCallback) {
    //   this.onSubmitCallback(form);
    // }
    if (!form.invalid) { this.dialogRef.close(this.wsState.workstation); }}
}




