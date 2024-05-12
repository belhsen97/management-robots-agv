import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { searchInputRangeDate } from 'src/app/core/store/actions/Global.Action';
import { getDateRangeSearchInput } from 'src/app/core/store/selectors/global.Selectors';
import { GlobalState, RangeDate } from 'src/app/core/store/states/Global.state';

@Component({
  selector: 'app-range-date',
  templateUrl: './range-date.component.html',
  styleUrls: ['./range-date.component.css']
})

export class RangeDateComponent implements OnInit , OnDestroy {
  public rangeDate !: RangeDate;
  private getDateRangeSearchInputSub !: Subscription | undefined;
  constructor(private store: Store<GlobalState>) { 
     this.rangeDate = { 
      start: new Date(2020, 6, 1),
      end: new Date(2020, 6, 3),
      limit: new Date(2016, 6, 16).getTime()-new Date(2016, 6, 12).getTime()};
    }
  ngOnInit(): void {
    this.getDateRangeSearchInputSub =  this.store.select(getDateRangeSearchInput).subscribe(input => { 
      this.rangeDate = input; 
    console.log("input");
    });
    
  }
  ngOnDestroy() {
    if (this.getDateRangeSearchInputSub) {this.getDateRangeSearchInputSub.unsubscribe();}}
 

  onApplyRange():void{ 
    this.store.dispatch(searchInputRangeDate({ rangeDate:  this.rangeDate }));
  }

}
