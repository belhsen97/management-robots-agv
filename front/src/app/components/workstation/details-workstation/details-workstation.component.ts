import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { ShowAlert } from 'src/app/core/store/Global/App.Action';
import { getRouterId } from 'src/app/core/store/Router/Router.Seletor';
import { MsgReponseStatus } from 'src/app/core/store/models/Global/MsgReponseStatus.model';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';

@Component({
  selector: 'app-details-workstation',
  templateUrl: './details-workstation.component.html',
  styleUrls: ['./details-workstation.component.css']
})
export class DetailsWorkstationComponent implements OnInit,OnDestroy   {
  routerSubscription!: Subscription;

  constructor( private store: Store,  public wsService: WorkstationService  ) {}

  ngOnInit(): void {
    this.routerSubscription = this.store.select(getRouterId).subscribe(item => {
      if (item === null || item === undefined ) { return; }
      this.wsService.getById(item).subscribe(
        (response) => {
          this.wsService.workstation = response.body;
          this.wsService.workstation.robots.forEach(r => r.createdAt = this.wsService.toDate(r.createdAt.toString()));
          console.log(this.wsService.workstation );
         }
        ,(error) => {
          this.wsService.msgReponseStatus =  { title : "Error", datestamp: new Date(),status : ReponseStatus.ERROR , message : error.message};
          this.store.dispatch(ShowAlert(this.wsService.msgReponseStatus));
        }) ; 
    });
    this.routerSubscription.unsubscribe();
  }
  ngOnDestroy(): void {if (this.routerSubscription) {this.routerSubscription.unsubscribe();}}
}
