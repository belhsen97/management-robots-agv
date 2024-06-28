import { Component, Inject, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-message-box-scheduled-datetime',
  templateUrl: './message-box-scheduled-datetime.component.html',
  styleUrls: ['./message-box-scheduled-datetime.component.css']
})
export class MessageBoxScheduledDatetimeComponent implements OnInit {
  public readonly regex = /^(0?[1-9]|[12][0-9]|3[01])[-/:](0?[1-9]|1[0-2])[-/:](\d{4}|\d{1,2})$/;
  public selected!: Date | null;
  public dateInput!: String | null;
  public timeInput!: String | null;
  private lastDate !: Date;
  private dateNow !: Date;
  constructor(public dialogRef: MatDialogRef<MessageBoxScheduledDatetimeComponent>, @Inject(MAT_DIALOG_DATA) public data: any) { }
  ngOnInit(): void {
    this.dateNow = new Date();
    this.selected = this.dateNow;
    this.dateInput = `${this.dateNow.getDate()}/${this.dateNow.getMonth() + 1}/${this.dateNow.getFullYear()}`
    this.timeInput = "08:00";
    this.lastDate = new Date();
    this.lastDate.setDate(this.lastDate.getDate() - 1);
  }

  FilterOldDate = (date: Date): boolean => { return (this.lastDate.getTime() <= date.getTime()) };

  onPickTime(form: NgForm): void {
    if (form.invalid) { return; }
    if (!this.selected) { return; }
    const parts = this.timeInput!.split(/[:]/);
    if (parts.length !== 2) { return; }
    const hours = parseInt(parts[0], 10);
    const minutes = parseInt(parts[1], 10);
    const dateFix : Date = new Date(this.selected.getFullYear(), this.selected.getMonth(), this.selected.getDate(), hours, minutes);
    this.dialogRef.close(dateFix);
  } 

  onDateChange() {
    const vlidate: Boolean = this.regex.test(this.dateInput!.toString())
    if (!vlidate) { return; }
    if (this.selected) {
      const newDate = this.parseDate(this.dateInput!.toString());
      if (newDate) {
        this.selected = newDate;
        console.log('Updated selected date:', this.selected);
      }
    }
  }
  onSelectedChange(newDate: Date | null) {
    if (!newDate) { return; }
    this.dateInput = `${newDate.getDate()}/${newDate.getMonth() + 1}/${newDate.getFullYear()}`
  }
  parseDate(dateString: string): Date | null {
    const parts = dateString.split(/[-/:]/);
    if (parts.length === 3) {
      const day = parseInt(parts[0], 10);
      const month = parseInt(parts[1], 10) - 1; // Month is zero-based in JS Date
      const year = parseInt(parts[2], 10);
      return new Date(year, month, day);
    }
    return null;
  }
}