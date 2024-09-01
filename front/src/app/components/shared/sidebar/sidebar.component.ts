import { state } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import {  getStateSlidebare } from 'src/app/core/store/selectors/Global.selector';


@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent  implements OnInit {
  hideSidebar : boolean = false;


  constructor(private store: Store) {

  }
  ngOnInit(): void {
    // this.store.select(getStateSlidebare).subscribe(state => { 
    //   this.hideSidebar = state;
    // });
  }
}
