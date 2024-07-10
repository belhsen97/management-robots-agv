import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NgForm } from '@angular/forms';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { TagService } from 'src/app/core/services/tag.service';
import { RobotService } from 'src/app/core/services/robot.service';
import { RobotState, robotState } from 'src/app/core/store/states/Robot.state';
import { TagState, tagState } from 'src/app/core/store/states/Tag.state';
import { WorkstationState, wsState } from 'src/app/core/store/states/Worstation.state';
import { Store } from '@ngrx/store';
import { getListRobot } from 'src/app/core/store/selectors/Robot.Selector';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
@Component({
  selector: 'app-add-workstation',
  templateUrl: './add-workstation.component.html',
  styleUrls: ['./add-workstation.component.css']
})
export class AddWorkstationComponent implements OnInit {
  tagState !: TagState;
  wsState !: WorkstationState;
  listRobot !: RobotDto[];
  constructor(public workstationService: WorkstationService,
    private storeRobot: Store<RobotState>,
    public dialogRef: MatDialogRef<AddWorkstationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.tagState = tagState;
    this.wsState = wsState;
  }
  ngOnInit(): void {
    this.storeRobot.select(getListRobot).subscribe(item => { this.listRobot = item;  });
    if (this.data.element != undefined) { this.wsState.workstation = this.data.element; return; }
    this.wsState.workstation = { id: "", name: "", enable: true, tags: []};
  }


  compareTags(tag1: any, tag2: any): boolean { return tag1 && tag2 ? tag1.id === tag2.id : tag1 === tag2; }
  compareRobots(r1: any, r2: any): boolean { return r1 && r2 ? r1.id === r2.id : r1 === r2; }


  closepopup() { this.dialogRef.close(null); }


  //onSubmitCallback: Function | undefined;
  onSubmitForm(form: NgForm): void {
    // console.log(form.invalid);
    // console.log(this.workstationService.workstation);
    // if (this.onSubmitCallback) {
    //   this.onSubmitCallback(form);
    // }
    if (!form.invalid) { this.dialogRef.close(this.wsState.workstation); }
  }
}




