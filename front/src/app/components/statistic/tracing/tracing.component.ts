import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { TraceService } from 'src/app/core/services/trace.service';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { getValueSearchInput } from 'src/app/core/store/selectors/global.Selectors';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { TraceDto } from 'src/app/core/store/models/Trace/TraceDto.model';
import { globalState } from 'src/app/core/store/states/Global.state';

@Component({
  selector: 'app-tracing',
  templateUrl: './tracing.component.html',
  styleUrls: ['./tracing.component.css']
})
export class TracingComponent implements OnInit ,  AfterViewInit, OnDestroy {
  @ViewChild("paginator") paginator!: MatPaginator;
  obs!: Observable<any>;
  dataSource !: MatTableDataSource<TraceDto>;

  constructor( private store: Store , public traceService : TraceService , private changeDetectorRef: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<TraceDto>(this.traceService.listTraces);
    this.obs = this.dataSource.connect();

    this.store.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSource == undefined ){return ; }
      this.dataSource.filter = value;
    });

    this.traceService.getAll().subscribe(
      (response) => { 
        this.traceService.listTraces = response.body; 
        this.traceService.listTraces.forEach((trace, index) => {
          trace.timestamp = this.traceService.toDate (trace.timestamp.toString() );});
        this.dataSource.data =  this.traceService.listTraces ;
      }
      ,(error) => {
        this.traceService.msgResponseStatus  =    { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        this.store.dispatch( ShowAlert(  this.traceService.msgResponseStatus ) ); 
        //this.traceService.goToComponent("/sign-in");
      }) ;
  }

  onClickDelete(id: any): void {
        this.traceService.delete(id).subscribe(
          (response) => {
            this.traceService.msgResponseStatus  = response.body;
            this.store.dispatch(ShowAlert( this.traceService.msgResponseStatus ));
            this.traceService.listTraces = this.traceService.listTraces.filter(item => item.id !== id);
            this.dataSource.data = this.traceService.listTraces;
          },
          (error: HttpErrorResponse) => {
            if ((error.status === 406) || (error.status === 403)) {  this.traceService.msgResponseStatus  = error.error; }
            else {
              this.traceService.msgResponseStatus  = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
            }
            this.store.dispatch(ShowAlert( this.traceService.msgResponseStatus )); console.log(error.status);
          }
        );
  }
  onClickDeleteAll(): void {
    // this.openDialogConfirmation('Confirmation', '  Would you want to delete work station equal ' + id + ' ?', '300ms', '500ms',
    //   () => {
    //     this.traceService.delete(id).subscribe(
    //       (response) => {
    //         this.traceService.msgReponseStatus = response.body;
    //         this.store.dispatch(ShowAlert(this.traceService.msgReponseStatus));
    //         this.traceService.listRobots = this.traceService.listRobots.filter(item => item.id !== id);
    //         this.dataSourceRobot.data = this.traceService.listRobots;
    //       },
    //       (error: HttpErrorResponse) => {
    //         if ((error.status === 406) || (error.status === 403)) { this.traceService.msgReponseStatus = error.error; }
    //         else {
    //           this.traceService.msgReponseStatus = { title: "Error", datestamp: new Date(), status: ReponseStatus.ERROR, message: error.message }
    //         }
    //         this.store.dispatch(ShowAlert(this.traceService.msgReponseStatus)); console.log(error.status);
    //       }
    //     );

    //   });
  }


  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.changeDetectorRef.detectChanges(); 
  }

  ngOnDestroy() {
    if (this.dataSource) {
      this.dataSource.disconnect();
    }
  }
}
