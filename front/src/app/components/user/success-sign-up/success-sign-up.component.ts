import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { getrouterinfo } from 'src/app/core/store/Router/Router.Seletor';

@Component({
  selector: 'app-success-sign-up',
  templateUrl: './success-sign-up.component.html',
  styleUrls: ['./success-sign-up.component.css']
})
export class SuccessSignUpComponent implements OnInit {
  email : string = "mail";
  constructor(private store: Store){}
  
  
  ngOnInit(): void {
    this.store.select(getrouterinfo).subscribe(item => {
     this.email = item.params['email'] ;
    });}
  }