import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { getrouterinfo } from 'src/app/core/store/selectors/Router.seletor';
import { GlobalState } from 'src/app/core/store/states/Global.state';

@Component({
  selector: 'app-success-sign-up',
  templateUrl: './success-sign-up.component.html',
  styleUrls: ['./success-sign-up.component.css']
})
export class SuccessSignUpComponent implements OnInit {
  email : string = "mail";
  constructor(private storeGlobal: Store<GlobalState>){}
  
  
  ngOnInit(): void {
    this.storeGlobal.select(getrouterinfo).subscribe(item => {
     this.email = item.params['email'] ;
    });}
  }