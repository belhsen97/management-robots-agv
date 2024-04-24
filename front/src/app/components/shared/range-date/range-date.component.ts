import { Component } from '@angular/core';
const today = new Date();
const month = today.getMonth();
const year = today.getFullYear();

@Component({
  selector: 'app-range-date',
  templateUrl: './range-date.component.html',
  styleUrls: ['./range-date.component.css']
})

export class RangeDateComponent {
  public start = new Date(year, month, 13);
  public end = new Date(year, month, 16);
}
