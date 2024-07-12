import { Component, Inject, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TagService } from 'src/app/core/services/tag.service';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { TagState, tagState } from 'src/app/core/store/states/Tag.state';
import { WorkstationState, wsState } from 'src/app/core/store/states/Worstation.state';

@Component({
  selector: 'app-add-tag',
  templateUrl: './add-tag.component.html',
  styleUrls: ['./add-tag.component.css']
})
export class AddTagComponent implements OnInit {
  tagState !: TagState;
  wsState !: WorkstationState;
  constructor(public tagService: TagService,
    public workstationService: WorkstationService,
    public dialogRef: MatDialogRef<AddTagComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { 
      this.tagState = tagState;  this.wsState = wsState;
    }

  ngOnInit(): void { if (this.data.element != undefined) { this.tagState.tag = this.data.element; return;}
  this.tagState.tag ={id:'',code:'',workstation:wsState.workstation};
}
  compareWorkstation(w1: any, w2: any): boolean { return w1 && w2 ? w1.id === w2.id : w1 === w2; }
  closepopup() { this.dialogRef.close(null); }
  onSubmitForm(form: NgForm): void { 
    if (!form.invalid) { this.dialogRef.close(this.tagState.tag); } 
  }
}
