import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { getRouterId } from 'src/app/core/store/selectors/Router.seletor';
import { WorkstationState, wsState } from 'src/app/core/store/states/Worstation.state';
import { GlobalState } from 'src/app/core/store/states/Global.state';

@Component({
  selector: 'app-details-workstation',
  templateUrl: './details-workstation.component.html',
  styleUrls: ['./details-workstation.component.css']
})
export class DetailsWorkstationComponent implements OnInit, OnDestroy {
  routerSubscription!: Subscription;
  wsState !: WorkstationState;
  constructor(private storeGlobal: Store<GlobalState>, public wsService: WorkstationService) {
    this.wsState = wsState;
  }

  ngOnInit(): void {
    this.routerSubscription = this.storeGlobal.select(getRouterId).subscribe(item => {
      if (item === null || item === undefined) { return; }
      this.wsService.getById(item).subscribe(
        (response) => {
          this.wsState.workstation = response.body;
          console.log(this.wsState.workstation);
        });
    });
    this.routerSubscription.unsubscribe();
  }
  ngOnDestroy(): void { if (this.routerSubscription) { this.routerSubscription.unsubscribe(); } }
}
