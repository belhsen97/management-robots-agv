import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { getRouterId } from 'src/app/core/store/Router/Router.Seletor';
import { MsgResponseStatus } from 'src/app/core/store/models/Global/MsgResponseStatus.model';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { globalState } from 'src/app/core/store/states/Global.state';
import { WorkstationState, wsState } from 'src/app/core/store/states/Worstation.state';

@Component({
  selector: 'app-details-workstation',
  templateUrl: './details-workstation.component.html',
  styleUrls: ['./details-workstation.component.css']
})
export class DetailsWorkstationComponent implements OnInit,OnDestroy   {
  routerSubscription!: Subscription;
  wsState !: WorkstationState;
  constructor( private store: Store,  public wsService: WorkstationService  ) {
    this.wsState = wsState;
  }

  ngOnInit(): void {
    this.routerSubscription = this.store.select(getRouterId).subscribe(item => {
      if (item === null || item === undefined ) { return; }
      this.wsService.getById(item).subscribe(
        (response) => {
          this.wsState.workstation = response.body;
          this.wsState.workstation.robots.forEach(r => r.createdAt = this.wsService.toDate(r.createdAt.toString()));
          console.log(this.wsState.workstation );
         }
        ,(error) => {
          this.wsService.msgResponseStatus  =  { title : "Error", datestamp: new Date(),status : ReponseStatus.ERROR , message : error.message};
          this.store.dispatch(ShowAlert(this.wsService.msgResponseStatus ));
        }) ; 
    });
    this.routerSubscription.unsubscribe();
  }
  ngOnDestroy(): void {if (this.routerSubscription) {this.routerSubscription.unsubscribe();}}
}
