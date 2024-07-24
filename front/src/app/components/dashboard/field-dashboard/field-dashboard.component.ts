import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';

@Component({
  selector: 'app-field-dashboard',
  templateUrl: './field-dashboard.component.html',
  styleUrls: ['./field-dashboard.component.css']
})
export class FieldDashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  x !:any;
  ngOnInit(): void {
   this.x =  document.getElementById("play-board-id");
 // this.x.innerHTML = ` <div   class="robot-dsign"> </div>`;

  }
  ngAfterViewInit(): void {
  }
  ngOnDestroy(): void {
  }
  
}
