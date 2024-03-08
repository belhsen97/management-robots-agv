import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-message-box-confirmation',
  templateUrl: './message-box-confirmation.component.html',
  styleUrls: ['./message-box-confirmation.component.css']
})
export class MessageBoxConfirmationComponent {
  constructor(public dialogRef: MatDialogRef<MessageBoxConfirmationComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {}
}
