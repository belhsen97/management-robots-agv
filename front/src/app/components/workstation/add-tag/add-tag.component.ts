import { Component, Inject, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TagService } from 'src/app/core/services/tag.service';
import { WorkstationService } from 'src/app/core/services/workstation.service';

@Component({
  selector: 'app-add-tag',
  templateUrl: './add-tag.component.html',
  styleUrls: ['./add-tag.component.css']
})
export class AddTagComponent implements OnInit {
  constructor(public tagService: TagService,
    public workstationService: WorkstationService,
    public dialogRef: MatDialogRef<AddTagComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void { if (this.data.element != undefined) { this.tagService.tag = this.data.element; } }
  compareWorkstation(w1: any, w2: any): boolean { return w1 && w2 ? w1.id === w2.id : w1 === w2; }
  closepopup() { this.dialogRef.close(null); }
  onSubmitForm(form: NgForm): void { if (!form.invalid) { this.dialogRef.close(this.tagService.tag); } }
}
